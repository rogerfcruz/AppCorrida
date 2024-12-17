package com.AppCorrida.AppCorrida.entities.enums;

public enum RideStatus {

    WAITING(1),
    IN_PROGRESS(2),
    CANCELED(3),
    FINISHED(4);

    private int codigo;

    private RideStatus(int codigo){
        this.codigo = codigo;
    }

    public static RideStatus valueOf(int codigo) {
        for (RideStatus valor : RideStatus.values()) {
            if (valor.getCodigo() == codigo) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Ride Status Code invalid.");
    }

    public int getCodigo() {
        return codigo;
    }
}
