package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.entities.Pauta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseListPautaRequestDTO() {
        List<Pauta> listaEnvio = List.of(pauta);
        List<PautaRequestDTO> listaRetorno = PautaMapper.parseListPautaRequestDTO(listaEnvio);
        PautaRequestDTO retorno = listaRetorno.getFirst();

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
    void parsePautaRequestDTO() {
        PautaRequestDTO retorno = PautaMapper.parsePautaRequestDTO(pauta);

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
    void parsePauta() {

        PautaRequestDTO pautaRequestDTO = new PautaRequestDTO();

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
