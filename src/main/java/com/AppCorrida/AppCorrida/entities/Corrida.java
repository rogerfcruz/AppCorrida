package com.AppCorrida.AppCorrida.entities;

import com.AppCorrida.AppCorrida.entities.enums.StatusCorrida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Entity
@Table(name = "tb_corrida")
@NoArgsConstructor
@AllArgsConstructor
public class Corrida implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Autowired
    private Passageiro passageiro;

    @Autowired
    private Motorista motorista;

    private Double latitude;

    private Double longitude;

    private Integer statusCorrida;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public StatusCorrida getStatusCorrida() {
        return statusCorrida != null ? StatusCorrida.valueOf(statusCorrida) : null;
    }

    public void setStatusCorrida(StatusCorrida statusCorrida) {
        if (statusCorrida != null){
            this.statusCorrida = statusCorrida.getCodigo();
        }
    }
}