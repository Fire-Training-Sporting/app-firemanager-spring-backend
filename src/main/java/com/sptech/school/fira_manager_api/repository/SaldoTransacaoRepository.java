package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.SaldoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaldoTransacaoRepository extends JpaRepository<SaldoTransacao, Long> {

    List<SaldoTransacao> findBySaldoIdOrderByCriadoEmAsc(Long saldoId);

    List<SaldoTransacao> findBySaldoIdAndQuantidadeRestanteGreaterThanAndDataExpiracaoGreaterThanEqualOrderByDataExpiracaoAsc(Long saldoId, Double quantidadeRestante, LocalDate hoje);

    List<SaldoTransacao> findByDataExpiracaoBeforeAndQuantidadeRestanteGreaterThan(LocalDate hoje, Double quantidadeRestante);
}

