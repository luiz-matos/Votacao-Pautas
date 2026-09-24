package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.PautaMapper;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.repositories.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private static final int TAMANHO_MAXIMO_TITULO = 255;
    private static final long MINUTOS_EM_ABERTO_PADRAO = 1;

    private final PautaRepository pautaRepository;

    public List<PautaDTO> listar() {
        return PautaMapper.parseListaPautaDTO(pautaRepository.listarComVotos());
    }

    public PautaDTO buscarPorId(Long id) {
        return PautaMapper.parsePautaDTO(buscarEntidade(id));
    }

    public PautaDTO salvar(PautaDTO pauta) {
        validaCriacao(pauta);
        // Só título, descrição e minutos vêm do cliente: id, prazo e cancelamento
        // mudam apenas pelos endpoints de início da votação e de cancelamento.
        Pauta novaPauta = new Pauta();
        novaPauta.setTitulo(pauta.getTitulo());
        novaPauta.setDescricao(pauta.getDescricao());
        novaPauta.setMinutosEmAberto(pauta.getMinutosEmAberto());
        return PautaMapper.parsePautaDTO(pautaRepository.save(novaPauta));
    }

    public void iniciarVotacao(Long id) {
        Pauta pauta = buscarEntidade(id);
        validaInicioVotacao(pauta);
        long minutos = pauta.getMinutosEmAberto() == null ? MINUTOS_EM_ABERTO_PADRAO : pauta.getMinutosEmAberto();
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(minutos));
        pautaRepository.save(pauta);
    }

    public void cancelar(Long id, String motivo) {
        Pauta pauta = buscarEntidade(id);
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta já cancelada.");
        }
        pauta.setCancelado(true);
        pauta.setMotivoCancelamento(motivo);
        pautaRepository.save(pauta);
    }

    private Pauta buscarEntidade(Long id) {
        return pautaRepository.buscarPorIdComVotos(id).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada."));
    }

    private static void validaCriacao(PautaDTO pauta) {
        if (estaVazio(pauta.getTitulo()) || estaVazio(pauta.getDescricao())) {
            throw new IllegalArgumentException("Pauta deve conter um título e descrição");
        }
        if (pauta.getTitulo().length() > TAMANHO_MAXIMO_TITULO) {
            throw new IllegalArgumentException("Título deve ter no máximo " + TAMANHO_MAXIMO_TITULO + " caracteres.");
        }
        if (pauta.getMinutosEmAberto() != null && pauta.getMinutosEmAberto() <= 0) {
            throw new IllegalArgumentException("Minutos em aberto deve ser maior que zero.");
        }
    }

    private static void validaInicioVotacao(Pauta pauta) {
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta cancelada.");
        }
        if (pauta.getTempoLimiteEmAberto() == null) {
            return;
        }
        if (pauta.getTempoLimiteEmAberto().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Pauta está em votação.");
        }
        throw new IllegalArgumentException("Pauta encerrada.");
    }

    private static boolean estaVazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
