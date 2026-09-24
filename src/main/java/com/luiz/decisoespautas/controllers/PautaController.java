package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.dtos.v1.CancelamentoPautaRequestDTO;
import com.luiz.decisoespautas.dtos.v1.PautaRequestDTO;
import com.luiz.decisoespautas.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pauta")
@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Operation(summary = "Lista todas as pautas")
    @GetMapping
    public List<PautaRequestDTO> listar() {
        return pautaService.listar();
    }

    @Operation(summary = "Lista todas as pautas")
    @GetMapping("/{id}")
    public PautaRequestDTO getAllPautas(@PathVariable("id") Long id) {
        return pautaService.encontraPorId(id);
    }

    @Operation(summary = "Salva informações de uma pauta")
    @PostMapping
    public PautaRequestDTO save(@RequestBody PautaRequestDTO pauta) {
        return pautaService.salvar(pauta);
    }

    @Operation(summary = "Cancela uma pauta")
    @PostMapping("/cancelamento")
    public void cancelarPauta(@RequestBody CancelamentoPautaRequestDTO cancelamentoPautaRequestDTO) {
        pautaService.cancelarPauta(cancelamentoPautaRequestDTO.getId(), cancelamentoPautaRequestDTO.getMotivoCancelamento());
    }

    @Operation(summary = "Habilita a votação de uma pauta")
    @PatchMapping()
    public void ativarVotacao(@PathParam("id") Long id) {
        pautaService.ativarVotacao(id);
    }
}
