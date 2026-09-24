package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.PautaMapper;
import com.luiz.decisoespautas.repositories.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public List<PautaRequestDTO> listar() {
        return PautaMapper.parseListPautaRequestDTO(pautaRepository.listarPautasComVotos());
    }

    public PautaRequestDTO encontraPorId(Long id) {
        return PautaMapper.parsePautaRequestDTO(pautaRepository.encontrarPautasPorIdComVotos(id).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada.")));
    }

    public PautaRequestDTO salvar(PautaRequestDTO pauta) {
        if (pauta.getTitulo() == null || pauta.getDescricao() == null) {
            throw new IllegalArgumentException("Pauta deve conter um título e descrição");
        }
        return PautaMapper.parsePautaRequestDTO(pautaRepository.save(PautaMapper.parsePauta(pauta)));
    }

    public void ativarVotacao(Long id) {
        PautaRequestDTO pauta = encontraPorId(id);
        validacaoPauta(pauta);
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(pauta.getMinutosEmAberto() == null ? 1 : pauta.getMinutosEmAberto()));
        pautaRepository.save(PautaMapper.parsePauta(pauta));
    }

    public void cancelarPauta(Long id, String motivo) {
        PautaRequestDTO pauta = encontraPorId(id);
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta já cancelada.");
        }
        pauta.setCancelado(true);
        pauta.setMotivoCancelamento(motivo);
        pautaRepository.save(PautaMapper.parsePauta(pauta));
    }

    private static void validacaoPauta(PautaRequestDTO pauta) {
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta cancelada.");
        }
        if (pauta.getTempoLimiteEmAberto() != null) {
            if (pauta.getTempoLimiteEmAberto().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("Pauta está em votação.");
            } else {
                throw new IllegalArgumentException("Pauta encerrada.");
            }
        }
    }

}
