package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaRequestDTO;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class VotoSessaoPautaMapperTest {

    @Mock
    private Pauta pauta;

    @Mock
    private PautaRequestDTO pautaRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseVotoSessaoPautaRequestDTO() {

        VotoSessaoPauta votoSessaoPauta = new VotoSessaoPauta();

        votoSessaoPauta.setId(20L);
        votoSessaoPauta.setVotoPositivo(true);
        votoSessaoPauta.setCpf("01234567890");
        votoSessaoPauta.setPauta(pauta);

        VotoSessaoPautaRequestDTO retorno = VotoSessaoPautaMapper.parseVotoSessaoPautaRequestDTO(votoSessaoPauta);

        assertEquals(votoSessaoPauta.getId(), retorno.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), retorno.getVotoPositivo());
        assertEquals(votoSessaoPauta.getCpf(), retorno.getCpf());
    }

    @Test
    void parseVotoSessaoPauta() {

        VotoSessaoPautaRequestDTO votoSessaoPautaRequestDTO = new VotoSessaoPautaRequestDTO();

        votoSessaoPautaRequestDTO.setId(20L);
        votoSessaoPautaRequestDTO.setVotoPositivo(true);
        votoSessaoPautaRequestDTO.setCpf("01234567890");
        votoSessaoPautaRequestDTO.setPauta(pautaRequestDTO);

        VotoSessaoPauta retorno = VotoSessaoPautaMapper.parseVotoSessaoPauta(votoSessaoPautaRequestDTO);

        assertEquals(votoSessaoPautaRequestDTO.getId(), retorno.getId());
        assertEquals(votoSessaoPautaRequestDTO.getVotoPositivo(), retorno.getVotoPositivo());
        assertEquals(votoSessaoPautaRequestDTO.getCpf(), retorno.getCpf());

    }
}
