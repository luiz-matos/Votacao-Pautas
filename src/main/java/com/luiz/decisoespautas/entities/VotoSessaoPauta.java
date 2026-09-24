package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_voto_pauta_cpf", columnNames = {"id_pauta", "cpf"}))
@Data
public class VotoSessaoPauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean votoPositivo;

    @Column(length = 11)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id_pauta", nullable = false)
    private Pauta pauta;
}
