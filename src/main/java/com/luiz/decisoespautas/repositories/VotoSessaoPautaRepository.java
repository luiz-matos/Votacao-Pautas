package com.luiz.decisoespautas.repositories;

import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoSessaoPautaRepository extends JpaRepository<VotoSessaoPauta, Long> {
    @Query("SELECT count(*) FROM VotoSessaoPauta V WHERE V.pauta.id = ?1 AND V.cpf = ?2")
    int existeVotoUsuarioNaSessao(Long idPauta, String cpf);
}
