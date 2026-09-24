package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.dtos.v1.VotoSessaoPautaRequestDTO;
import com.luiz.decisoespautas.service.VotoSessaoPautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voto")
@Tag(name = "Voto da Pauta", description = "Endpoints para gerenciamento de votos de uma pauta")
public class VotoSessaoPautaController {
    @Autowired
    private VotoSessaoPautaService votoSessaoPautaService;

    @Operation(summary = "Encontra um voto pelo id")
    @GetMapping
    public VotoSessaoPautaRequestDTO find(@PathParam("id") Long id) {
        return votoSessaoPautaService.encontraPorId(id);
    }

    @Operation(summary = "Salva um voto de uma pauta")
    @PostMapping
    public VotoSessaoPautaRequestDTO save(@RequestBody VotoSessaoPautaRequestDTO votoSessaoPauta) {
        return votoSessaoPautaService.save(votoSessaoPauta);
    }
}
