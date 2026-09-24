package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.VotoSessaoPautaMapper;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoSessaoPautaServiceTest {

    PautaDTO pauta;
    PautaDTO pautaCancelada;
    VotoSessaoPautaDTO votoSessaoPauta;

    @InjectMocks
    private VotoSessaoPautaService votoSessaoPautaService;
    @Mock
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Mock
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        pauta = new PautaDTO();
        pauta.setId(1L);
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descrição");
        pauta.setMinutosEmAberto(10L);
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(10));


        pautaCancelada = new PautaDTO();
        pautaCancelada.setId(2L);
        pautaCancelada.setTitulo("Titulo Cancelada");
        pautaCancelada.setDescricao("Descrição Cancelada");
        pautaCancelada.setCancelado(true);

        votoSessaoPauta = new VotoSessaoPautaDTO();
        votoSessaoPauta.setId(5L);
        votoSessaoPauta.setVotoPositivo(true);
        votoSessaoPauta.setCpf("01234567890");
        votoSessaoPauta.setPauta(pauta);

    }

    @Test
    void testEncontraPorId() {
        when(pautaService.buscarPorId(pauta.getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.findById(votoSessaoPauta.getId())).thenReturn(Optional.of(VotoSessaoPautaMapper.parseVotoSessaoPauta(votoSessaoPauta)));
        VotoSessaoPautaDTO votoSessaoPautaTest = votoSessaoPautaService.buscarPorId(votoSessaoPauta.getId());

        assertEquals(votoSessaoPauta.getId(), votoSessaoPautaTest.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), votoSessaoPautaTest.getVotoPositivo());
        assertEquals(votoSessaoPauta.getCpf(), votoSessaoPautaTest.getCpf());

        PautaDTO pautaTest = votoSessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }


    @Test
    void testEncontraPorIdNotFound() {
        when(votoSessaoPautaRepository.findById(votoSessaoPauta.getId())).thenReturn(Optional.empty());
        Long idPauta = votoSessaoPauta.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            votoSessaoPautaService.buscarPorId(idPauta);
        });

        String erroEsperado = "Voto não encontrado.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSalvar() {
        when(pautaService.buscarPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.existsByPautaIdAndCpf(pauta.getId(), votoSessaoPauta.getCpf())).thenReturn(false);
        when(votoSessaoPautaRepository.save(any(VotoSessaoPauta.class))).thenReturn(VotoSessaoPautaMapper.parseVotoSessaoPauta(votoSessaoPauta));

        VotoSessaoPautaDTO votoSessaoPautaTest = votoSessaoPautaService.salvar(votoSessaoPauta);

        assertEquals(votoSessaoPauta.getId(), votoSessaoPautaTest.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), votoSessaoPautaTest.getVotoPositivo());
        assertEquals(votoSessaoPauta.getCpf(), votoSessaoPautaTest.getCpf());

        PautaDTO pautaTest = votoSessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testSalvarCpfInvalido() {
        votoSessaoPauta.setCpf("invalido");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.salvar(votoSessaoPauta);
        });

        String erroEsperado = "CPF inválido.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSalvarCpfComLetras() {
        votoSessaoPauta.setCpf("ABCDEFGHI45");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoSessaoPautaService.salvar(votoSessaoPauta));
        assertEquals("CPF inválido.", exception.getMessage());
    }

    @Test
    void testSalvarSemCpf() {
        votoSessaoPauta.setCpf(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoSessaoPautaService.salvar(votoSessaoPauta));
        assertEquals("CPF inválido.", exception.getMessage());
    }

    @Test
    void testSalvarSemPauta() {
        votoSessaoPauta.setPauta(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoSessaoPautaService.salvar(votoSessaoPauta));
        assertEquals("Voto deve informar a pauta.", exception.getMessage());
    }

    @Test
    void testSalvarSemSimOuNao() {
        votoSessaoPauta.setVotoPositivo(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoSessaoPautaService.salvar(votoSessaoPauta));
        assertEquals("Voto deve ser sim (true) ou não (false).", exception.getMessage());
    }

    @Test
    void testSalvarVotoSimultaneoBarradoPeloBanco() {
        when(pautaService.buscarPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.existsByPautaIdAndCpf(pauta.getId(), votoSessaoPauta.getCpf())).thenReturn(false);
        when(votoSessaoPautaRepository.save(any(VotoSessaoPauta.class))).thenThrow(new DataIntegrityViolationException("uk_voto_pauta_cpf"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoSessaoPautaService.salvar(votoSessaoPauta));
        assertEquals("Usuário já votou nesta pauta.", exception.getMessage());
    }

    @Test
    void testSalvarUsuarioJaVotou() {
        when(pautaService.buscarPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.existsByPautaIdAndCpf(pauta.getId(), votoSessaoPauta.getCpf())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.salvar(votoSessaoPauta);
        });

        String erroEsperado = "Usuário já votou nesta pauta.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSalvarPautaCancelada() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(30));
        when(pautaService.buscarPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pautaCancelada);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.salvar(votoSessaoPauta);
        });

        String erroEsperado = "Pauta cancelada.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSalvarVotoPautaNaoIniciada() {
        pauta.setTempoLimiteEmAberto(null);
        when(pautaService.buscarPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.salvar(votoSessaoPauta);
        });

        String erroEsperado = "Pauta não iniciada.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSalvarTempoLimiteEsgotado() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(30));
        when(pautaService.buscarPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.salvar(votoSessaoPauta);
        });

        String erroEsperado = "Pauta encerrada.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }
}
