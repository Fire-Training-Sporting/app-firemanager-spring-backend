package com.sptech.school.fira_manager_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_professores")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Função é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EFuncaoProfessor funcao;

    public Professor() {
    }

    public Professor(Long id, String nome, EFuncaoProfessor funcao) {
        this.id = id;
        this.nome = nome;
        this.funcao = funcao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EFuncaoProfessor getFuncao() {
        return funcao;
    }

    public void setFuncao(EFuncaoProfessor funcao) {
        this.funcao = funcao;
    }
}