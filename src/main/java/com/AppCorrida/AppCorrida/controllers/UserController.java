package com.AppCorrida.AppCorrida.controllers;

import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.dto.UserDTO;
import com.AppCorrida.AppCorrida.entities.mapper.RideMapper;
import com.AppCorrida.AppCorrida.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{id}/rides")
    public ResponseEntity<UserDTO> getUserWithRides(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserWithRides(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        user = userService.updateUser(id, user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/ride/create")
    public ResponseEntity<Ride> createRide(@RequestBody Ride ride) {
        Ride createdRide = userService.createRide(ride);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdRide.getId()).toUri();
        return ResponseEntity.created(uri).body(createdRide);
    }

    @PutMapping("/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable Long rideId, @RequestParam Long userId) {
        Ride acceptedRide = userService.acceptRide(rideId, userId);
        return ResponseEntity.ok(acceptedRide);
    }

    @PutMapping("/{rideId}/cancel")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long rideId, @RequestParam Long userId) {
        Ride cancelRide = userService.cancelRide(rideId, userId);
        return ResponseEntity.ok(cancelRide);
    }

    @PutMapping("/{rideId}/finish")
    public ResponseEntity<Ride> finishRide(@PathVariable Long rideId, @RequestParam Long userId) {
        Ride finishRide = userService.finishRide(rideId, userId);
        return ResponseEntity.ok(finishRide);
    }
}