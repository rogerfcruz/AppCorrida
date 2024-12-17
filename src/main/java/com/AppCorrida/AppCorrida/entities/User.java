package com.AppCorrida.AppCorrida.entities;

import com.AppCorrida.AppCorrida.entities.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tb_user")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = true)
    private String carPlate;

    @OneToMany(mappedBy = "passenger")
    @JsonIgnore
    private List<Ride> passengerRides;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Ride> driverRides;

    public User(){}

    public User(Long id, @NonNull String name, String email, @NonNull String password, UserType userType, String carPlate, List<Ride> passengerRides, List<Ride> driverRides) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.carPlate = carPlate;
        this.passengerRides = passengerRides;
        this.driverRides = driverRides;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NonNull String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public @NonNull String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public List<Ride> getPassengerRides() {
        return passengerRides;
    }

    public List<Ride> getDriverRides() {
        return driverRides;
    }
}
