package com.AppCorrida.AppCorrida.controllers;

import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.dto.UserDTO;
import com.AppCorrida.AppCorrida.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll(@RequestParam int page, @RequestParam int items) {
        Pageable pageable = PageRequest.of(page, items);
        return ResponseEntity.ok().body(userService.findAll(pageable));
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

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/update/{id}")
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

    @PutMapping("/{userId}/ride/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable Long userId, @PathVariable Long rideId) {
        Ride acceptedRide = userService.acceptRide(rideId, userId);
        return ResponseEntity.ok(acceptedRide);
    }

    @PutMapping("/{userId}/ride/{rideId}/cancel")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long userId, @PathVariable Long rideId) {
        Ride cancelRide = userService.cancelRide(rideId, userId);
        return ResponseEntity.ok(cancelRide);
    }

    @PutMapping("/{userId}/ride/{rideId}/finish")
    public ResponseEntity<Ride> finishRide(@PathVariable Long userId, @PathVariable Long rideId) {
        Ride finishRide = userService.finishRide(rideId, userId);
        return ResponseEntity.ok(finishRide);
    }
}