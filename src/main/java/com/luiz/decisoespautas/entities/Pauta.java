package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 255)
    private String titulo;

    @Column(nullable = false ,columnDefinition = "text")
    private String descricao;

    @Column
    private LocalDateTime tempoLimiteEmAberto;

    @Column
    private Long minutosEmAberto;

    @Column(nullable = false)
    private boolean isCancelado = false;

    @Column(columnDefinition = "text")
    private String motivoCancelamento;

    @Transient
    private Long votosSim;

    @Transient
    private Long votosNao;

    public Pauta(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime tempoLimiteEmAberto,
        Long minutosEmAberto,
        boolean isCancelado,
        String motivoCancelamento,
        Long votosSim,
        Long votosNao
    ) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tempoLimiteEmAberto = tempoLimiteEmAberto;
        this.minutosEmAberto = minutosEmAberto;
        this.votosSim = votosSim;
        this.votosNao = votosNao;
        this.isCancelado = isCancelado;
        this.motivoCancelamento = motivoCancelamento;
    }
}
