package com.luiz.decisoespautas.dtos.v1.mappers;

import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaDTO;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class VotoSessaoPautaMapperTest {

    @Mock
    private Pauta pauta;

    @Mock
    private PautaDTO pautaRequestDTO;

    @BeforeEach
    void setUp() {
    }

    @Test
    void parseVotoSessaoPautaDTO() {

        VotoSessaoPauta votoSessaoPauta = new VotoSessaoPauta();

        votoSessaoPauta.setId(20L);
        votoSessaoPauta.setVotoPositivo(true);
        votoSessaoPauta.setCpf("01234567890");
        votoSessaoPauta.setPauta(pauta);

        VotoSessaoPautaDTO retorno = VotoSessaoPautaMapper.parseVotoSessaoPautaDTO(votoSessaoPauta);

        assertEquals(votoSessaoPauta.getId(), retorno.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), retorno.getVotoPositivo());
        assertEquals(votoSessaoPauta.getCpf(), retorno.getCpf());
    }

    @Test
    void parseVotoSessaoPauta() {

        VotoSessaoPautaDTO votoSessaoPautaDTO = new VotoSessaoPautaDTO();

        votoSessaoPautaDTO.setId(20L);
        votoSessaoPautaDTO.setVotoPositivo(true);
        votoSessaoPautaDTO.setCpf("01234567890");
        votoSessaoPautaDTO.setPauta(pautaRequestDTO);

        VotoSessaoPauta retorno = VotoSessaoPautaMapper.parseVotoSessaoPauta(votoSessaoPautaDTO);

        assertEquals(votoSessaoPautaDTO.getId(), retorno.getId());
        assertEquals(votoSessaoPautaDTO.getVotoPositivo(), retorno.getVotoPositivo());
        assertEquals(votoSessaoPautaDTO.getCpf(), retorno.getCpf());

    }
}
