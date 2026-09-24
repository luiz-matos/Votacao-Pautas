package com.luiz.decisoespautas.enums;

public enum ResultadoVotacao {
    APROVADA,
    REPROVADA,
    EMPATE;

    public static ResultadoVotacao de(long votosSim, long votosNao) {
        if (votosSim == votosNao) {
            return EMPATE;
        }
        return votosSim > votosNao ? APROVADA : REPROVADA;
    }
}
