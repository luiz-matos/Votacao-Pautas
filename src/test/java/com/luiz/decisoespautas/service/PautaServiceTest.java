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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(pautaRepository.save(pauta)).thenReturn(pauta);
        PautaRequestDTO pautaTest = pautaService.salvar(pautaRequestDTO);
        assertEquals(pautaRequestDTO.getId(), pautaTest.getId());
        assertEquals(pautaRequestDTO.getTitulo(), pautaTest.getTitulo());
        assertEquals(pautaRequestDTO.getDescricao(), pautaTest.getDescricao());
    }
}
