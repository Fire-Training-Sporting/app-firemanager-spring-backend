package com.sptech.school.fira_manager_api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_saldo_transacoes")
public class SaldoTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_saldo", nullable = false)
    private Saldo saldo;

    @Column(name = "quantidade_original", nullable = false)
    private Double quantidadeOriginal;

    @Column(name = "quantidade_restante", nullable = false)
    private Double quantidadeRestante;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDate dataExpiracao;

    @Column(name = "criado_em", nullable = false)
    private LocalDate criadoEm;

    public SaldoTransacao() {}

    public SaldoTransacao(Saldo saldo, Double quantidade, LocalDate dataExpiracao, LocalDate criadoEm) {
        this.saldo = saldo;
        this.quantidadeOriginal = quantidade;
        this.quantidadeRestante = quantidade;
        this.dataExpiracao = dataExpiracao;
        this.criadoEm = criadoEm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Saldo getSaldo() { return saldo; }
    public void setSaldo(Saldo saldo) { this.saldo = saldo; }

    public Double getQuantidadeOriginal() { return quantidadeOriginal; }

    public void setQuantidadeOriginal(Double quantidadeOriginal) { this.quantidadeOriginal = quantidadeOriginal; }

    public Double getQuantidadeRestante() { return quantidadeRestante; }

    public void setQuantidadeRestante(Double quantidadeRestante) { this.quantidadeRestante = quantidadeRestante; }

    public LocalDate getDataExpiracao() { return dataExpiracao; }

    public void setDataExpiracao(LocalDate dataExpiracao) { this.dataExpiracao = dataExpiracao; }

    public LocalDate getCriadoEm() { return criadoEm; }

    public void setCriadoEm(LocalDate criadoEm) { this.criadoEm = criadoEm; }
}