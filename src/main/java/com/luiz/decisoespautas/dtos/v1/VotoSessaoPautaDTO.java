package com.luiz.decisoespautas.dtos.v1;

import lombok.Data;

@Data
public class VotoSessaoPautaDTO {

    private Long id;
    private Boolean votoPositivo;
    private String cpf;
    private PautaDTO pauta;

}
