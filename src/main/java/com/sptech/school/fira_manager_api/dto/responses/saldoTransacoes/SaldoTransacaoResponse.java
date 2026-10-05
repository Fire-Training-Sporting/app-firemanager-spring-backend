package com.sptech.school.fira_manager_api.dto.responses.saldoTransacoes;

import java.time.LocalDate;

public class SaldoTransacaoResponse {

    private Long id;
    private Double quantidadeOriginal;
    private Double quantidadeRestante;
    private LocalDate dataExpiracao;
    private LocalDate criadoEm;

    public SaldoTransacaoResponse(Long id, Double quantidadeOriginal, Double quantidadeRestante, LocalDate dataExpiracao, LocalDate criadoEm) {
        this.id = id;
        this.quantidadeOriginal = quantidadeOriginal;
        this.quantidadeRestante = quantidadeRestante;
        this.dataExpiracao = dataExpiracao;
        this.criadoEm = criadoEm;
    }

    public Long getId() { return id; }
    public Double getQuantidadeOriginal() { return quantidadeOriginal; }
    public Double getQuantidadeRestante() { return quantidadeRestante; }
    public LocalDate getDataExpiracao() { return dataExpiracao; }
    public LocalDate getCriadoEm() { return criadoEm; }
}