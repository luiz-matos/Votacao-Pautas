package com.luiz.decisoespautas.enums;

import java.time.LocalDateTime;

public enum StatusPauta {
    NAO_INICIADA,
    EM_VOTACAO,
    ENCERRADA,
    CANCELADA;

    public static StatusPauta de(boolean cancelado, LocalDateTime tempoLimiteEmAberto) {
        if (cancelado) {
            return CANCELADA;
        }
        if (tempoLimiteEmAberto == null) {
            return NAO_INICIADA;
        }
        return tempoLimiteEmAberto.isAfter(LocalDateTime.now()) ? EM_VOTACAO : ENCERRADA;
    }
}
