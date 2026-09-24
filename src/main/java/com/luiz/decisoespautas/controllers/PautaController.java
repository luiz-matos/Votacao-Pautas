package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.dtos.v1.CancelamentoPautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.PautaDTO;
import com.luiz.decisoespautas.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pauta")
@RequiredArgsConstructor
@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
public class PautaController {

    private final PautaService pautaService;

    @Operation(summary = "Lista todas as pautas com a contagem de votos")
    @GetMapping
    public List<PautaDTO> listar() {
        return pautaService.listar();
    }

    @Operation(summary = "Busca uma pauta pelo id")
    @GetMapping("/{id}")
    public PautaDTO buscarPorId(@PathVariable Long id) {
        return pautaService.buscarPorId(id);
    }

    @Operation(summary = "Cria uma pauta")
    @PostMapping
    public PautaDTO salvar(@RequestBody PautaDTO pauta) {
        return pautaService.salvar(pauta);
    }

    @Operation(summary = "Cancela uma pauta")
    @PostMapping("/cancelamento")
    public void cancelar(@RequestBody CancelamentoPautaRequestDTO cancelamento) {
        pautaService.cancelar(cancelamento.getId(), cancelamento.getMotivoCancelamento());
    }

    @Operation(summary = "Inicia a votação de uma pauta")
    @PatchMapping
    public void iniciarVotacao(@RequestParam Long id) {
        pautaService.iniciarVotacao(id);
    }
}
