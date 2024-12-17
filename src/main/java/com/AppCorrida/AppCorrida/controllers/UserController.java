package com.AppCorrida.AppCorrida.controllers;

import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.dto.RideDTO;
import com.AppCorrida.AppCorrida.entities.dto.UserDTO;
import com.AppCorrida.AppCorrida.entities.enums.UserType;
import com.AppCorrida.AppCorrida.entities.mapper.RideMapper;
import com.AppCorrida.AppCorrida.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RideMapper rideMapper;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{id}/rides")
    public ResponseEntity<?> getUserWithRides(@PathVariable Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCarPlate(user.getCarPlate());

        List<RideDTO> passengerRides = user.getPassengerRides().stream()
                .map(rideMapper::convertToRideDTO)
                .collect(Collectors.toList());

        userDTO.setPassengerRides(passengerRides);

        List<RideDTO> driverRides = userService.getRidesForUser(user.getUserType()).stream()
                .map(rideMapper::convertToRideDTO)
                .collect(Collectors.toList());

        userDTO.setDriverRides(driverRides);

        if (user.getUserType() == UserType.PASSENGER && passengerRides.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no rides for the passenger.");
        }

        if (user.getUserType() == UserType.DRIVER && driverRides.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no rides in the waiting status for the driver.");
        }

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user){
        user = userService.update(id, user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/ride/create")
    public ResponseEntity<?> createRide(@RequestBody Ride ride) {
        try {
            Ride createdRide = userService.create(ride);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdRide.getId()).toUri();
            return ResponseEntity.created(uri).body(createdRide);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable Long rideId, @RequestParam Long userId) {
        Ride acceptedRide = userService.acceptRide(rideId, userId);

        if (acceptedRide == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(acceptedRide);
    }

    @PutMapping("/{rideId}/cancel")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long rideId, @RequestParam Long userId) {
        return ResponseEntity.ok(userService.cancelRide(rideId, userId));
    }

    @PutMapping("/{rideId}/finish")
    public ResponseEntity<Ride> finishRide(@PathVariable Long rideId, @RequestParam Long userId) {
        return ResponseEntity.ok(userService.finishRide(rideId, userId));
    }
}