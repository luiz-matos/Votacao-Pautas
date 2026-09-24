package com.luiz.decisoespautas.dtos.v1;

import lombok.Data;

@Data
public class VotoSessaoPautaRequestDTO {

    private Long id;
    private Boolean votoPositivo;
    private String cpf;
    private PautaRequestDTO pauta;

}
