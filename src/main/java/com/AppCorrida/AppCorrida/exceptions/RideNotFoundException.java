package com.AppCorrida.AppCorrida.exceptions;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(Long id) {
        super("Ride not found. ID: " + id);
    }
    public RideNotFoundException(String message) { super(message); }
}