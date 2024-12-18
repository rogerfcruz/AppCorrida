package com.AppCorrida.AppCorrida.entities.dto;

import java.util.List;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String carPlate;
    private List<RideDTO> passengerRides;
    private List<RideDTO> driverRides;

    public UserDTO(){}

    public UserDTO(Long id, String name, String email, String carPlate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.carPlate = carPlate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public List<RideDTO> getPassengerRides() { return passengerRides; }

    public void setPassengerRides(List<RideDTO> passengerRides) {
        this.passengerRides = passengerRides;
    }

    public List<RideDTO> getDriverRides() {
        return driverRides;
    }

    public void setDriverRides(List<RideDTO> driverRides) {
        this.driverRides = driverRides;
    }
}
