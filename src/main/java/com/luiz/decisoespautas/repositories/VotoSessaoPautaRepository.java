package com.luiz.decisoespautas.repositories;

import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoSessaoPautaRepository extends JpaRepository<VotoSessaoPauta, Long> {

    boolean existsByPautaIdAndCpf(Long idPauta, String cpf);
}
