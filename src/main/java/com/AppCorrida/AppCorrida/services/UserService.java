package com.AppCorrida.AppCorrida.services;

import com.AppCorrida.AppCorrida.entities.Destination;
import com.AppCorrida.AppCorrida.entities.Origin;
import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.dto.RideDTO;
import com.AppCorrida.AppCorrida.entities.dto.UserDTO;
import com.AppCorrida.AppCorrida.entities.enums.RideStatus;
import com.AppCorrida.AppCorrida.entities.enums.UserType;
import com.AppCorrida.AppCorrida.entities.mapper.RideMapper;
import com.AppCorrida.AppCorrida.exceptions.*;
import com.AppCorrida.AppCorrida.repositories.RideRepository;
import com.AppCorrida.AppCorrida.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RideMapper rideMapper;

    public List<User> findAll(Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);
        return page.getContent();
    }

    public User findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserDTO getUserWithRides(Long id){
        User user = findById(id);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCarPlate(user.getCarPlate());

        List<RideDTO> passengerRides = user.getPassengerRides().stream()
                .map(rideMapper::convertToRideDTO)
                .collect(Collectors.toList());

        userDTO.setPassengerRides(passengerRides);

        List<RideDTO> driverRides = getRidesForUser(user.getUserType()).stream()
                .map(rideMapper::convertToRideDTO)
                .collect(Collectors.toList());

        userDTO.setDriverRides(driverRides);

        if (user.getUserType() == UserType.PASSENGER && passengerRides.isEmpty()){
            throw new RideNotFoundException("There are no rides for the passenger.");
        }

        if (user.getUserType() == UserType.DRIVER && driverRides.isEmpty()){
            throw new RideNotFoundException("There are no rides in the waiting status for the driver.");
        }

        return userDTO;
    }

    public List<Ride> getRidesForUser(UserType userType) {
        if (userType == UserType.DRIVER) {
            return rideRepository.findByStatus(RideStatus.WAITING);
        } else {
            return Collections.emptyList();
        }
    }

    public User createUser(User user) {
        validateUser(user);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CreateUserException("Email already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        validateUser(user);

        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            updatedUser.setCarPlate(user.getCarPlate());
            updatedUser.setUserType(user.getUserType());
            return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new CreateUserException("Name is required.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new CreateUserException("Email is required.");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new CreateUserException("Password is required.");
        }

        if (user.getUserType() == null) {
            throw new CreateUserException("User type is required.");
        }

        if (user.getUserType() == UserType.DRIVER && (user.getCarPlate() == null || user.getCarPlate().trim().isEmpty())) {
            throw new CreateUserException("Car plate is required for drivers.");
        }
    }

    public Ride createRide(Ride ride) {
        if (ride.getPassenger() == null || ride.getPassenger().getId() == null) {
            throw new CreateRideException("Passenger ID is required to create a ride.");
        }

        User passenger = userRepository.findById(ride.getPassenger().getId()).orElseThrow(() -> new UserNotFoundException(ride.getPassenger().getId()));
        ride.setPassenger(passenger);

        boolean thereIsARaceInProgress = rideRepository.existsByPassengerIdAndStatus(ride.getPassenger().getId(), RideStatus.IN_PROGRESS) ||
                rideRepository.existsByPassengerIdAndStatus(ride.getPassenger().getId(), RideStatus.WAITING);

        if (thereIsARaceInProgress) {
            throw new CreateRideException("Passenger already has a ride in progress.");
        }
        ride.setStatus(RideStatus.WAITING);

        return rideRepository.save(ride);
    }

    public Ride acceptRide(Long rideId, Long userId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideNotFoundException(rideId));
        User driver = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (ride.getStatus() != RideStatus.WAITING) {
            throw new AcceptRideException("Only rides in waiting can be accepted.");
        }

        List<Ride> onGoingRides = rideRepository.findByDriverIdAndStatus(userId, RideStatus.IN_PROGRESS);
        if (!onGoingRides.isEmpty()) {
            throw new AcceptRideException("There is a ride in progress, you cannot accept until it is finished.");
        }

        if (driver.getUserType() != UserType.DRIVER) {
            throw new AcceptRideException("Only drivers can be accept rides.");
        }

        ride.setDriver(driver);
        ride.setCarPlate(driver.getCarPlate());
        ride.setStatus(RideStatus.IN_PROGRESS);
        return rideRepository.save(ride);
    }

    public Ride cancelRide(Long rideId, Long userId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideNotFoundException(rideId));
        User passenger = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (passenger.getUserType() != UserType.PASSENGER) {
            throw new CancelRideException("Only passengers can be cancel rides.");
        }

        if (!ride.getPassenger().getId().equals(userId)) {
            throw new CancelRideException("This is not your ride.");
        }

        if (ride.getStatus() != RideStatus.WAITING) {
            throw new CancelRideException("Only rides in waiting can be cancelled.");
        }

        ride.setStatus(RideStatus.CANCELED);
        return rideRepository.save(ride);
    }

    public Ride finishRide(Long rideId, Long userId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideNotFoundException(rideId));
        User driver = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (driver.getUserType() != UserType.DRIVER) {
            throw new FinishRideException("Only drivers can be finish rides.");
        }

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new FinishRideException("Only rides in progress can be finished.");
        }

        ride.setStatus(RideStatus.FINISHED);

        double distance = calculateDistance(ride.getOrigin(), ride.getDestination());
        double price = calculatePrice(distance, 2);

        ride.setDistance(distance);
        ride.setPrice(price);
        return rideRepository.save(ride);
    }

    private double calculateDistance(Origin origin, Destination destination) {
        final String API_KEY = "5b3ce3597851110001cf62486c326c9568644fa7b8dc70d779d757dc";
        final String BASE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

        double[] originArray = {origin.getLatitude(), origin.getLongitude()};
        double[] destinationArray = {destination.getLatitude(), destination.getLongitude()};

        String url = BASE_URL + "?start=" + originArray[1] + "," + originArray[0] + "&end=" + destinationArray[1] + "," + destinationArray[0];

        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", API_KEY);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {

            String responseString = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseString);

            JsonNode segments = jsonResponse.get("features")
                    .get(0)
                    .get("properties")
                    .get("segments")
                    .get(0);

            return segments.get("distance").asDouble();
        } catch (IOException e) {
            throw new CalculateDistanceRideException(e.getMessage());
        }
    }

    public double calculatePrice(double distance, double priceForKM){
        return Math.floor((distance / 1000 * priceForKM) * 100) / 100;
    }
}