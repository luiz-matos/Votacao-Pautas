package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaDTO;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;

public class VotoSessaoPautaMapper {

    private VotoSessaoPautaMapper() {}

    public static VotoSessaoPautaDTO parseVotoSessaoPautaDTO(VotoSessaoPauta votoSessaoPauta) {
        VotoSessaoPautaDTO votoSessaoPautaDTO = new VotoSessaoPautaDTO();

        votoSessaoPautaDTO.setId(votoSessaoPauta.getId());
        votoSessaoPautaDTO.setVotoPositivo(votoSessaoPauta.getVotoPositivo());
        votoSessaoPautaDTO.setCpf(votoSessaoPauta.getCpf());
        votoSessaoPautaDTO.setPauta(PautaMapper.parsePautaDTO(votoSessaoPauta.getPauta()));

        return votoSessaoPautaDTO;
    }

    public static VotoSessaoPauta parseVotoSessaoPauta(VotoSessaoPautaDTO votoSessaoPautaDTO) {
        VotoSessaoPauta votoSessaoPauta = new VotoSessaoPauta();

        votoSessaoPauta.setId(votoSessaoPautaDTO.getId());
        votoSessaoPauta.setVotoPositivo(votoSessaoPautaDTO.getVotoPositivo());
        votoSessaoPauta.setCpf(votoSessaoPautaDTO.getCpf());
        votoSessaoPauta.setPauta(PautaMapper.parsePauta(votoSessaoPautaDTO.getPauta()));

        return votoSessaoPauta;
    }
}
