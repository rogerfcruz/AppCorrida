package com.AppCorrida.AppCorrida.entities.dto;

public class RideDTO {

    private Long id;
    private String status;
    private OriginDTO origin;
    private DestinationDTO destination;
    private UserDTO driver;

    public RideDTO(){}

    public RideDTO(Long id, String status, OriginDTO origin, DestinationDTO destination, UserDTO driver) {
        this.id = id;
        this.status = status;
        this.origin = origin;
        this.destination = destination;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OriginDTO getOrigin() {
        return origin;
    }

    public void setOrigin(OriginDTO origin) { this.origin = origin; }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDestination(DestinationDTO destination) { this.destination = destination; }

    public UserDTO getDriver() {
        return driver;
    }

    public void setDriver(UserDTO driver) {
        this.driver = driver;
    }
}
