package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.VotoSessaoPautaMapper;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.enums.StatusPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
import com.luiz.decisoespautas.utils.ValidaCpf;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotoSessaoPautaService {

    private static final String MENSAGEM_JA_VOTOU = "Usuário já votou nesta pauta.";

    private final VotoSessaoPautaRepository votoSessaoPautaRepository;
    private final PautaService pautaService;

    public VotoSessaoPautaDTO buscarPorId(Long id) {
        VotoSessaoPauta voto = votoSessaoPautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Voto não encontrado."));
        return comPautaAtualizada(VotoSessaoPautaMapper.parseVotoSessaoPautaDTO(voto));
    }

    public VotoSessaoPautaDTO salvar(VotoSessaoPautaDTO voto) {
        validaVoto(voto);

        PautaDTO pauta = pautaService.buscarPorId(voto.getPauta().getId());
        validaPautaAbertaParaVoto(pauta);
        voto.setPauta(pauta);

        if (votoSessaoPautaRepository.existsByPautaIdAndCpf(pauta.getId(), voto.getCpf())) {
            throw new IllegalArgumentException(MENSAGEM_JA_VOTOU);
        }
        VotoSessaoPauta votoSalvo;
        try {
            votoSalvo = votoSessaoPautaRepository.save(VotoSessaoPautaMapper.parseVotoSessaoPauta(voto));
        } catch (DataIntegrityViolationException e) {
            // Dois votos simultâneos do mesmo CPF passam pela consulta acima; o índice único do banco barra o segundo
            throw new IllegalArgumentException(MENSAGEM_JA_VOTOU);
        }
        return comPautaAtualizada(VotoSessaoPautaMapper.parseVotoSessaoPautaDTO(votoSalvo));
    }

    // Recarrega a pauta para a resposta trazer a contagem de votos atual
    private VotoSessaoPautaDTO comPautaAtualizada(VotoSessaoPautaDTO voto) {
        voto.setPauta(pautaService.buscarPorId(voto.getPauta().getId()));
        return voto;
    }

    private static void validaVoto(VotoSessaoPautaDTO voto) {
        if (!ValidaCpf.isCPF(voto.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (voto.getPauta() == null || voto.getPauta().getId() == null) {
            throw new IllegalArgumentException("Voto deve informar a pauta.");
        }
        if (voto.getVotoPositivo() == null) {
            throw new IllegalArgumentException("Voto deve ser sim (true) ou não (false).");
        }
    }

    private static void validaPautaAbertaParaVoto(PautaDTO pauta) {
        switch (StatusPauta.de(pauta.isCancelado(), pauta.getTempoLimiteEmAberto())) {
            case CANCELADA -> throw new IllegalArgumentException("Pauta cancelada.");
            case NAO_INICIADA -> throw new IllegalArgumentException("Pauta não iniciada.");
            case ENCERRADA -> throw new IllegalArgumentException("Pauta encerrada.");
            case EM_VOTACAO -> { }
        }
    }
}
