package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaRequestDTO;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;

public class VotoSessaoPautaMapper {

    private VotoSessaoPautaMapper() {}

    public static VotoSessaoPautaRequestDTO parseVotoSessaoPautaRequestDTO(VotoSessaoPauta votoSessaoPauta) {
        VotoSessaoPautaRequestDTO votoSessaoPautaRequestDTO = new VotoSessaoPautaRequestDTO();

        votoSessaoPautaRequestDTO.setId(votoSessaoPauta.getId());
        votoSessaoPautaRequestDTO.setVotoPositivo(votoSessaoPauta.getVotoPositivo());
        votoSessaoPautaRequestDTO.setCpf(votoSessaoPauta.getCpf());
        votoSessaoPautaRequestDTO.setPauta(PautaMapper.parsePautaRequestDTO(votoSessaoPauta.getPauta()));

        return votoSessaoPautaRequestDTO;
    }

    public static VotoSessaoPauta parseVotoSessaoPauta(VotoSessaoPautaRequestDTO votoSessaoPautaRequestDTO) {
        VotoSessaoPauta votoSessaoPauta = new VotoSessaoPauta();

        votoSessaoPauta.setId(votoSessaoPautaRequestDTO.getId());
        votoSessaoPauta.setVotoPositivo(votoSessaoPautaRequestDTO.getVotoPositivo());
        votoSessaoPauta.setCpf(votoSessaoPautaRequestDTO.getCpf());
        votoSessaoPauta.setPauta(PautaMapper.parsePauta(votoSessaoPautaRequestDTO.getPauta()));

        return votoSessaoPauta;
    }
}
