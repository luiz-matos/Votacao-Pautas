package com.luiz.decisoespautas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column
    private LocalDateTime tempoLimiteEmAberto;

    @Column
    private Long minutosEmAberto;

    @Column(name = "is_cancelado", nullable = false)
    private boolean cancelado;

    @Column(columnDefinition = "text")
    private String motivoCancelamento;

    // Contagens calculadas pelas consultas do PautaRepository, não são colunas
    @Transient
    private Long votosSim;

    @Transient
    private Long votosNao;
}
