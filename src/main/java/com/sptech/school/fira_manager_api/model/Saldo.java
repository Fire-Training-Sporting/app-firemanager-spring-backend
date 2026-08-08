package com.sptech.school.fira_manager_api.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_saldo_servicos")
public class Saldo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_servico", nullable = false)
    private Servico servico;


    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario aluno;

    @NotNull
    @Column(name = "quantidade",  nullable = false)
    private Double quantidade;

    public Saldo() {
    }

    public Saldo(Long id, Servico servico, Usuario aluno, Double quantidade) {
        this.id = id;
        this.servico = servico;
        this.aluno = aluno;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }
}
