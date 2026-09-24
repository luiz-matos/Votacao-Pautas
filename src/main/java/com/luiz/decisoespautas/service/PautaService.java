package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.PautaMapper;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.repositories.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaService {

    private static final int TAMANHO_MAXIMO_TITULO = 255;

    @Autowired
    private PautaRepository pautaRepository;

    public List<PautaRequestDTO> listar() {
        return PautaMapper.parseListPautaRequestDTO(pautaRepository.listarPautasComVotos());
    }

    public PautaRequestDTO encontraPorId(Long id) {
        return PautaMapper.parsePautaRequestDTO(pautaRepository.encontrarPautasPorIdComVotos(id).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada.")));
    }

    public PautaRequestDTO salvar(PautaRequestDTO pauta) {
        if (pauta.getTitulo() == null || pauta.getTitulo().isBlank() || pauta.getDescricao() == null || pauta.getDescricao().isBlank()) {
            throw new IllegalArgumentException("Pauta deve conter um título e descrição");
        }
        if (pauta.getTitulo().length() > TAMANHO_MAXIMO_TITULO) {
            throw new IllegalArgumentException("Título deve ter no máximo " + TAMANHO_MAXIMO_TITULO + " caracteres.");
        }
        if (pauta.getMinutosEmAberto() != null && pauta.getMinutosEmAberto() <= 0) {
            throw new IllegalArgumentException("Minutos em aberto deve ser maior que zero.");
        }
        // Só título, descrição e minutos vêm do cliente: id, prazo e cancelamento
        // mudam apenas pelos endpoints de início da votação e de cancelamento.
        Pauta novaPauta = new Pauta();
        novaPauta.setTitulo(pauta.getTitulo());
        novaPauta.setDescricao(pauta.getDescricao());
        novaPauta.setMinutosEmAberto(pauta.getMinutosEmAberto());
        return PautaMapper.parsePautaRequestDTO(pautaRepository.save(novaPauta));
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
