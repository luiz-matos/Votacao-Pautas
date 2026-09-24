package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.enums.ResultadoVotacao;
import com.luiz.decisoespautas.enums.StatusPauta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class PautaMapperTest {

    private Pauta pauta;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(10L);
        pauta.setTitulo("Titulo Parse");
        pauta.setDescricao("Descricao Parse");
        pauta.setTempoLimiteEmAberto(LocalDateTime.now());
        pauta.setMinutosEmAberto(3L);
        pauta.setCancelado(false);
        pauta.setMotivoCancelamento("Motivo Parse");
        pauta.setVotosSim(11L);
        pauta.setVotosNao(12L);
    }

    @Test
    void parseListaPautaDTO() {
        List<Pauta> listaEnvio = List.of(pauta);
        List<PautaDTO> listaRetorno = PautaMapper.parseListaPautaDTO(listaEnvio);
        PautaDTO retorno = listaRetorno.getFirst();

        assertFalse(listaRetorno.isEmpty());

        assertEquals(pauta.getId(), retorno.getId());
        assertEquals(pauta.getTitulo(), retorno.getTitulo());
        assertEquals(pauta.getDescricao(), retorno.getDescricao());
        assertEquals(pauta.getTempoLimiteEmAberto(), retorno.getTempoLimiteEmAberto());
        assertEquals(pauta.getMinutosEmAberto(), retorno.getMinutosEmAberto());
        assertEquals(pauta.isCancelado(), retorno.isCancelado());
        assertEquals(pauta.getMotivoCancelamento(), retorno.getMotivoCancelamento());
        assertEquals(pauta.getVotosSim(), retorno.getVotosSim());
        assertEquals(pauta.getVotosNao(), retorno.getVotosNao());

    }

    @Test
    void parsePautaDTO() {
        PautaDTO retorno = PautaMapper.parsePautaDTO(pauta);

        assertEquals(pauta.getId(), retorno.getId());
        assertEquals(pauta.getTitulo(), retorno.getTitulo());
        assertEquals(pauta.getDescricao(), retorno.getDescricao());
        assertEquals(pauta.getTempoLimiteEmAberto(), retorno.getTempoLimiteEmAberto());
        assertEquals(pauta.getMinutosEmAberto(), retorno.getMinutosEmAberto());
        assertEquals(pauta.isCancelado(), retorno.isCancelado());
        assertEquals(pauta.getMotivoCancelamento(), retorno.getMotivoCancelamento());
        assertEquals(pauta.getVotosSim(), retorno.getVotosSim());
        assertEquals(pauta.getVotosNao(), retorno.getVotosNao());
    }

    @Test
    void statusEResultadoDaPautaEncerrada() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(1));
        PautaDTO retorno = PautaMapper.parsePautaDTO(pauta);

        assertEquals(StatusPauta.ENCERRADA, retorno.getStatus());
        assertEquals(ResultadoVotacao.REPROVADA, retorno.getResultado());
    }

    @Test
    void semResultadoEnquantoEmVotacao() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(1));
        PautaDTO retorno = PautaMapper.parsePautaDTO(pauta);

        assertEquals(StatusPauta.EM_VOTACAO, retorno.getStatus());
        assertNull(retorno.getResultado());
    }

    @Test
    void statusDaPautaCanceladaENaoIniciada() {
        pauta.setTempoLimiteEmAberto(null);
        assertEquals(StatusPauta.NAO_INICIADA, PautaMapper.parsePautaDTO(pauta).getStatus());

        pauta.setCancelado(true);
        assertEquals(StatusPauta.CANCELADA, PautaMapper.parsePautaDTO(pauta).getStatus());
    }

    @Test
    void resultadoDaVotacao() {
        assertEquals(ResultadoVotacao.APROVADA, ResultadoVotacao.de(3, 2));
        assertEquals(ResultadoVotacao.REPROVADA, ResultadoVotacao.de(2, 3));
        assertEquals(ResultadoVotacao.EMPATE, ResultadoVotacao.de(0, 0));
    }

    @Test
    void parsePauta() {

        PautaDTO pautaRequestDTO = new PautaDTO();

        pautaRequestDTO.setId(10L);
        pautaRequestDTO.setTitulo("Titulo Parse");
        pautaRequestDTO.setDescricao("Descricao Parse");
        pautaRequestDTO.setTempoLimiteEmAberto(LocalDateTime.now());
        pautaRequestDTO.setMinutosEmAberto(3L);
        pautaRequestDTO.setCancelado(false);
        pautaRequestDTO.setMotivoCancelamento("Motivo Parse");
        pautaRequestDTO.setVotosSim(11L);
        pautaRequestDTO.setVotosNao(12L);

        Pauta retorno = PautaMapper.parsePauta(pautaRequestDTO);

        assertEquals(pautaRequestDTO.getId(), retorno.getId());
        assertEquals(pautaRequestDTO.getTitulo(), retorno.getTitulo());
        assertEquals(pautaRequestDTO.getDescricao(), retorno.getDescricao());
        assertEquals(pautaRequestDTO.getTempoLimiteEmAberto(), retorno.getTempoLimiteEmAberto());
        assertEquals(pautaRequestDTO.getMinutosEmAberto(), retorno.getMinutosEmAberto());
        assertEquals(pautaRequestDTO.isCancelado(), retorno.isCancelado());
        assertEquals(pautaRequestDTO.getMotivoCancelamento(), retorno.getMotivoCancelamento());
        assertEquals(pautaRequestDTO.getVotosSim(), retorno.getVotosSim());
        assertEquals(pautaRequestDTO.getVotosNao(), retorno.getVotosNao());
    }
}
