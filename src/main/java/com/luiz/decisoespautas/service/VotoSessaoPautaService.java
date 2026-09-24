package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.VotoSessaoPautaMapper;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
import com.luiz.decisoespautas.utils.ValidaCpf;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotoSessaoPautaService {
    private static final String MENSAGEM_JA_VOTOU = "Usuário já votou nesta pauta.";

    @Autowired
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Autowired
    private PautaService pautaService;

    public VotoSessaoPautaRequestDTO encontraPorId(Long id) {
        VotoSessaoPauta voto = votoSessaoPautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Voto não encontrado."));
        return comPautaAtualizada(VotoSessaoPautaMapper.parseVotoSessaoPautaRequestDTO(voto));
    }

    public VotoSessaoPautaRequestDTO save(VotoSessaoPautaRequestDTO votoSessaoPauta) {
        validaCpf(votoSessaoPauta.getCpf());
        validaVoto(votoSessaoPauta);

        PautaRequestDTO pauta = pautaService.encontraPorId(votoSessaoPauta.getPauta().getId());
        validaPauta(pauta);
        votoSessaoPauta.setPauta(pauta);

        validaSeUsuarioJaVotouNaSessao(votoSessaoPauta.getPauta().getId(), votoSessaoPauta.getCpf());
        VotoSessaoPauta votoSalvo;
        try {
            votoSalvo = votoSessaoPautaRepository.save(VotoSessaoPautaMapper.parseVotoSessaoPauta(votoSessaoPauta));
        } catch (DataIntegrityViolationException e) {
            // Dois votos simultâneos do mesmo CPF passam pela consulta acima; a restrição única do banco barra o segundo
            throw new IllegalArgumentException(MENSAGEM_JA_VOTOU);
        }
        return comPautaAtualizada(VotoSessaoPautaMapper.parseVotoSessaoPautaRequestDTO(votoSalvo));
    }

    // Recarrega a pauta para a resposta trazer a contagem de votos atual
    private VotoSessaoPautaRequestDTO comPautaAtualizada(VotoSessaoPautaRequestDTO voto) {
        voto.setPauta(pautaService.encontraPorId(voto.getPauta().getId()));
        return voto;
    }

    private void validaCpf(String cpf) {
        if (!ValidaCpf.isCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    private void validaVoto(VotoSessaoPautaRequestDTO voto) {
        if (voto.getPauta() == null || voto.getPauta().getId() == null) {
            throw new IllegalArgumentException("Voto deve informar a pauta.");
        }
        if (voto.getVotoPositivo() == null) {
            throw new IllegalArgumentException("Voto deve ser sim (true) ou não (false).");
        }
    }

    private void validaSeUsuarioJaVotouNaSessao(Long idPauta, String cpf) {
        int quantidadeVoto = votoSessaoPautaRepository.existeVotoUsuarioNaSessao(idPauta, cpf);
        if (quantidadeVoto != 0) {
            throw new IllegalArgumentException(MENSAGEM_JA_VOTOU);
        }
    }

    private void validaPauta(PautaRequestDTO pauta) {
        validaSePautaCancelada(pauta);
        validaSePautaNaoIniciada(pauta);
        validaSePautaEncerrada(pauta);
    }

    private void validaSePautaCancelada(PautaRequestDTO pauta) {
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta cancelada.");
        }
    }

    private void validaSePautaNaoIniciada(PautaRequestDTO pauta) {
        if (pauta.getTempoLimiteEmAberto() == null) {
            throw new IllegalArgumentException("Pauta não iniciada.");
        }
    }

    private void validaSePautaEncerrada(PautaRequestDTO pauta) {
        if (pauta.getTempoLimiteEmAberto().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Pauta encerrada.");
        }
    }
}
