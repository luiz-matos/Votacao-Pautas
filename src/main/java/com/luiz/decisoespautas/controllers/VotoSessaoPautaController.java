package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaDTO;
import com.luiz.decisoespautas.service.VotoSessaoPautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voto")
@RequiredArgsConstructor
@Tag(name = "Voto da Pauta", description = "Endpoints para gerenciamento de votos de uma pauta")
public class VotoSessaoPautaController {

    private final VotoSessaoPautaService votoSessaoPautaService;

    @Operation(summary = "Busca um voto pelo id")
    @GetMapping
    public VotoSessaoPautaDTO buscarPorId(@RequestParam Long id) {
        return votoSessaoPautaService.buscarPorId(id);
    }

    @Operation(summary = "Registra o voto de um CPF em uma pauta")
    @PostMapping
    public VotoSessaoPautaDTO salvar(@RequestBody VotoSessaoPautaDTO voto) {
        return votoSessaoPautaService.salvar(voto);
    }
}
