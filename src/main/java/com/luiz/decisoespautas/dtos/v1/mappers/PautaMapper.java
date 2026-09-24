package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.entities.Pauta;

import java.util.ArrayList;
import java.util.List;

public class PautaMapper {

    private PautaMapper() {}

    public static List<PautaRequestDTO> parseListPautaRequestDTO(List<Pauta> pautasListDTO) {
        List<PautaRequestDTO> pautasDTO = new ArrayList<>();
        for (Pauta pautaRequestDTO : pautasListDTO) {
            pautasDTO.add(parsePautaRequestDTO(pautaRequestDTO));
        }
        return pautasDTO;
    }

    public static PautaRequestDTO parsePautaRequestDTO(Pauta pauta) {
        PautaRequestDTO pautaRequestDTO = new PautaRequestDTO();

        pautaRequestDTO.setId(pauta.getId());
        pautaRequestDTO.setTitulo(pauta.getTitulo());
        pautaRequestDTO.setDescricao(pauta.getDescricao());
        pautaRequestDTO.setTempoLimiteEmAberto(pauta.getTempoLimiteEmAberto());
        pautaRequestDTO.setMinutosEmAberto(pauta.getMinutosEmAberto());
        pautaRequestDTO.setCancelado(pauta.isCancelado());
        pautaRequestDTO.setMotivoCancelamento(pauta.getMotivoCancelamento());
        pautaRequestDTO.setVotosSim(pauta.getVotosSim());
        pautaRequestDTO.setVotosNao(pauta.getVotosNao());

        return pautaRequestDTO;
    }

    public static Pauta parsePauta(PautaRequestDTO pautaRequestDTO) {
        Pauta pauta = new Pauta();

        pauta.setId(pautaRequestDTO.getId());
        pauta.setTitulo(pautaRequestDTO.getTitulo());
        pauta.setDescricao(pautaRequestDTO.getDescricao());
        pauta.setTempoLimiteEmAberto(pautaRequestDTO.getTempoLimiteEmAberto());
        pauta.setMinutosEmAberto(pautaRequestDTO.getMinutosEmAberto());
        pauta.setCancelado(pautaRequestDTO.isCancelado());
        pauta.setMotivoCancelamento(pautaRequestDTO.getMotivoCancelamento());
        pauta.setVotosSim(pautaRequestDTO.getVotosSim());
        pauta.setVotosNao(pautaRequestDTO.getVotosNao());

        return pauta;
    }
}
