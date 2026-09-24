package com.luiz.decisoespautas.dtos.v1;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PautaRequestDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime tempoLimiteEmAberto;
    private Long minutosEmAberto;
    private boolean isCancelado = false;
    private String motivoCancelamento;
    private Long votosSim;
    private Long votosNao;
}
