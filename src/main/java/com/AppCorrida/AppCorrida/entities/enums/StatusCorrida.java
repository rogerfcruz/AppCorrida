package com.AppCorrida.AppCorrida.entities.enums;

public enum StatusCorrida {

    AGUARDANDO(1),
    EM_ANDAMENTO(2),
    CANCELADA(3),
    FINALIZADA(4);

    private int codigo;

    private StatusCorrida(int codigo){
        this.codigo = codigo;
    }

    public static StatusCorrida valueOf(int codigo) {
        for (StatusCorrida valor : StatusCorrida.values()) {
            if (valor.getCodigo() == codigo) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Código de status de corrida inválido.");
    }

    public int getCodigo() {
        return codigo;
    }
}
