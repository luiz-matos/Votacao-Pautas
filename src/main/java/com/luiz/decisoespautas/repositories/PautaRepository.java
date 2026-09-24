package com.luiz.decisoespautas.repositories;

import com.luiz.decisoespautas.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

    String SELECT_PAUTA_COM_VOTOS =
        "SELECT new Pauta(" +
            "pauta.id, pauta.titulo, pauta.descricao, pauta.tempoLimiteEmAberto, pauta.minutosEmAberto, " +
            "pauta.cancelado, pauta.motivoCancelamento, " +
            "SUM(CASE WHEN voto.votoPositivo = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN voto.votoPositivo = false THEN 1 ELSE 0 END)" +
        ") " +
        "FROM Pauta pauta " +
        "LEFT JOIN VotoSessaoPauta voto ON voto.pauta.id = pauta.id ";

    // O PostgreSQL aceita agrupar só pela chave primária: as outras colunas dependem dela
    String GROUP_BY_PAUTA = " GROUP BY pauta.id";

    @Query(SELECT_PAUTA_COM_VOTOS + GROUP_BY_PAUTA)
    List<Pauta> listarComVotos();

    @Query(SELECT_PAUTA_COM_VOTOS + "WHERE pauta.id = ?1" + GROUP_BY_PAUTA)
    Optional<Pauta> buscarPorIdComVotos(Long id);
}
