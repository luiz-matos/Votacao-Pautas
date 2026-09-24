package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.enums.ResultadoVotacao;
import com.luiz.decisoespautas.enums.StatusPauta;

import java.util.List;

public class PautaMapper {

    private PautaMapper() {}

    public static List<PautaDTO> parseListaPautaDTO(List<Pauta> pautas) {
        return pautas.stream().map(PautaMapper::parsePautaDTO).toList();
    }

    public static PautaDTO parsePautaDTO(Pauta pauta) {
        PautaDTO pautaDTO = new PautaDTO();

        pautaDTO.setId(pauta.getId());
        pautaDTO.setTitulo(pauta.getTitulo());
        pautaDTO.setDescricao(pauta.getDescricao());
        pautaDTO.setTempoLimiteEmAberto(pauta.getTempoLimiteEmAberto());
        pautaDTO.setMinutosEmAberto(pauta.getMinutosEmAberto());
        pautaDTO.setCancelado(pauta.isCancelado());
        pautaDTO.setMotivoCancelamento(pauta.getMotivoCancelamento());
        pautaDTO.setVotosSim(pauta.getVotosSim());
        pautaDTO.setVotosNao(pauta.getVotosNao());
        pautaDTO.setStatus(StatusPauta.de(pauta.isCancelado(), pauta.getTempoLimiteEmAberto()));
        if (pautaDTO.getStatus() == StatusPauta.ENCERRADA && pauta.getVotosSim() != null && pauta.getVotosNao() != null) {
            pautaDTO.setResultado(ResultadoVotacao.de(pauta.getVotosSim(), pauta.getVotosNao()));
        }

        return pautaDTO;
    }

    public static Pauta parsePauta(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();

        pauta.setId(pautaDTO.getId());
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        pauta.setTempoLimiteEmAberto(pautaDTO.getTempoLimiteEmAberto());
        pauta.setMinutosEmAberto(pautaDTO.getMinutosEmAberto());
        pauta.setCancelado(pautaDTO.isCancelado());
        pauta.setMotivoCancelamento(pautaDTO.getMotivoCancelamento());
        pauta.setVotosSim(pautaDTO.getVotosSim());
        pauta.setVotosNao(pautaDTO.getVotosNao());

        return pauta;
    }
}
