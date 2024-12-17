package com.AppCorrida.AppCorrida.services;

import com.AppCorrida.AppCorrida.entities.Destination;
import com.AppCorrida.AppCorrida.entities.Origin;
import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.enums.RideStatus;
import com.AppCorrida.AppCorrida.entities.enums.UserType;
import com.AppCorrida.AppCorrida.exceptions.ResourceNotFoundException;
import com.AppCorrida.AppCorrida.repositories.RideRepository;
import com.AppCorrida.AppCorrida.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(Long id, User user){
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            updatedUser.setPassword(user.getPassword());

            updatedUser.setCarPlate(user.getCarPlate());
            updatedUser.setUserType(user.getUserType());
            return userRepository.save(updatedUser);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public List<Ride> getRidesForUser(UserType userType) {
        if (userType == UserType.DRIVER) {
            return rideRepository.findByStatus(RideStatus.WAITING);
        } else {
            return Collections.emptyList();
        }
    }

    public Ride create(Ride ride) {
        if (ride.getPassenger() == null || ride.getPassenger().getId() == null) {
            throw new RuntimeException("Passenger ID is required to create a ride.");
        }

        User passenger = userRepository.findById(ride.getPassenger().getId()).orElseThrow(() -> new RuntimeException("Passenger not found"));
        ride.setPassenger(passenger);

        boolean thereIsARaceInProgress = rideRepository.existsByPassengerIdAndStatus(ride.getPassenger().getId(), RideStatus.IN_PROGRESS) ||
                rideRepository.existsByPassengerIdAndStatus(ride.getPassenger().getId(), RideStatus.WAITING);

        if (thereIsARaceInProgress) {
            throw new RuntimeException("Passenger already has a ride in progress.");
        }
        ride.setStatus(RideStatus.WAITING);

        return rideRepository.save(ride);
    }

    public Ride acceptRide(Long rideId, Long userId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new ResourceNotFoundException(rideId));
        User driver = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));

        if (ride.getStatus() != RideStatus.WAITING) {
            throw new RuntimeException("Only rides in waiting can be accepted.");
        }

        List<Ride> onGoingRides = rideRepository.findByDriverIdAndStatus(userId, RideStatus.IN_PROGRESS);
        if (!onGoingRides.isEmpty()) {
            throw new RuntimeException("There is a ride in progress, you cannot accept until it is finished.");
        }

        if (driver.getUserType() != UserType.DRIVER) {
            throw new RuntimeException("Only drivers can be accept rides.");
        }

        ride.setDriver(driver);
        ride.setCarPlate(driver.getCarPlate());
        ride.setStatus(RideStatus.IN_PROGRESS);
        return rideRepository.save(ride);
    }

    public Ride cancelRide(Long rideId, Long userId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new ResourceNotFoundException(rideId));
        User passenger = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));

        if (passenger.getUserType() != UserType.PASSENGER) {
            throw new RuntimeException("Only passengers can be cancel rides.");
        }

        if (!ride.getPassenger().getId().equals(userId)) {
            throw new RuntimeException("This is not your ride.");
        }

        if (ride.getStatus() != RideStatus.WAITING) {
            throw new RuntimeException("Only rides in waiting can be cancelled.");
        }

        ride.setStatus(RideStatus.CANCELED);
        return rideRepository.save(ride);
    }

    public Ride finishRide(Long rideId, Long userId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new ResourceNotFoundException(rideId));
        User driver = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));

        if (driver.getUserType() != UserType.DRIVER) {
            throw new RuntimeException("Only drivers can be finish rides.");
        }

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new RuntimeException("Only rides in progress can be finished.");
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
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double calculatePrice(double distance, double priceForKM){
        return Math.floor((distance / 1000 * priceForKM) * 100) / 100;
    }
}