package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.mappers.PautaMapper;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.repositories.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    PautaRequestDTO pautaRequestDTO;
    Pauta pauta;
    Pauta pautaCancelada;

    @InjectMocks
    private PautaService pautaService;
    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {
        pautaRequestDTO = new PautaRequestDTO();
        pautaRequestDTO.setId(1L);
        pautaRequestDTO.setTitulo("Titulo");
        pautaRequestDTO.setDescricao("Descrição");

        pauta = PautaMapper.parsePauta(pautaRequestDTO);

        PautaRequestDTO pautaCanceladaDTO = new PautaRequestDTO();
        pautaCanceladaDTO.setId(2L);
        pautaCanceladaDTO.setTitulo("Titulo Cancelada");
        pautaCanceladaDTO.setDescricao("Descrição Cancelada");
        pautaCanceladaDTO.setCancelado(true);

        pautaCancelada = PautaMapper.parsePauta(pautaCanceladaDTO);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListar() {
        when(pautaRepository.listarPautasComVotos()).thenReturn(List.of(pauta));
        PautaRequestDTO pautaTest = pautaService.listar().getFirst();
        assertEquals(pautaRequestDTO.getId(), pautaTest.getId());
        assertEquals(pautaRequestDTO.getTitulo(), pautaTest.getTitulo());
        assertEquals(pautaRequestDTO.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testEncontraPorId() {
        when(pautaRepository.encontrarPautasPorIdComVotos(pautaRequestDTO.getId())).thenReturn(Optional.of(pauta));
        PautaRequestDTO pautaTest = pautaService.encontraPorId(pautaRequestDTO.getId());
        assertEquals(pautaRequestDTO.getId(), pautaTest.getId());
        assertEquals(pautaRequestDTO.getTitulo(), pautaTest.getTitulo());
        assertEquals(pautaRequestDTO.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testEncontraPorIdNotFound() {
        when(pautaRepository.encontrarPautasPorIdComVotos(pautaRequestDTO.getId())).thenReturn(Optional.empty());
        Long idPauta = pautaRequestDTO.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> pautaService.encontraPorId(idPauta));
        String erroEsperado = "Pauta não encontrada.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testPautaCancelada() {
        pautaRequestDTO.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(2L));
        when(pautaRepository.encontrarPautasPorIdComVotos(pautaCancelada.getId())).thenReturn(Optional.of(pautaCancelada));
        Long idPauta = pautaCancelada.getId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.ativarVotacao(idPauta));
        String erroEsperado = "Pauta cancelada.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testPautaVotacaoEncerrada() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(2L));
        when(pautaRepository.encontrarPautasPorIdComVotos(pautaRequestDTO.getId())).thenReturn(Optional.of(pauta));
        Long idPauta = pautaRequestDTO.getId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.ativarVotacao(idPauta));
        String erroEsperado = "Pauta encerrada.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testPautaEmVotacao() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(30L));
        when(pautaRepository.encontrarPautasPorIdComVotos(pautaRequestDTO.getId())).thenReturn(Optional.of(pauta));
        Long idPauta = pautaRequestDTO.getId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.ativarVotacao(idPauta));
        String erroEsperado = "Pauta está em votação.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSalvar() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        PautaRequestDTO pautaTest = pautaService.salvar(pautaRequestDTO);
        assertEquals(pautaRequestDTO.getId(), pautaTest.getId());
        assertEquals(pautaRequestDTO.getTitulo(), pautaTest.getTitulo());
        assertEquals(pautaRequestDTO.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testSalvarIgnoraCamposControladosPelaApi() {
        pautaRequestDTO.setCancelado(true);
        pautaRequestDTO.setMotivoCancelamento("Motivo");
        pautaRequestDTO.setTempoLimiteEmAberto(LocalDateTime.now().plusYears(1));
        pautaRequestDTO.setMinutosEmAberto(5L);
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        pautaService.salvar(pautaRequestDTO);

        ArgumentCaptor<Pauta> salva = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository).save(salva.capture());
        assertNull(salva.getValue().getId());
        assertFalse(salva.getValue().isCancelado());
        assertNull(salva.getValue().getMotivoCancelamento());
        assertNull(salva.getValue().getTempoLimiteEmAberto());
        assertEquals(5L, salva.getValue().getMinutosEmAberto());
    }

    @Test
    void testSalvarTituloEmBranco() {
        pautaRequestDTO.setTitulo("   ");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.salvar(pautaRequestDTO));
        assertEquals("Pauta deve conter um título e descrição", exception.getMessage());
    }

    @Test
    void testSalvarTituloLongo() {
        pautaRequestDTO.setTitulo("x".repeat(256));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.salvar(pautaRequestDTO));
        assertEquals("Título deve ter no máximo 255 caracteres.", exception.getMessage());
    }

    @Test
    void testSalvarMinutosInvalidos() {
        pautaRequestDTO.setMinutosEmAberto(0L);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.salvar(pautaRequestDTO));
        assertEquals("Minutos em aberto deve ser maior que zero.", exception.getMessage());
    }
}
