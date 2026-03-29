package com.sptech.school.fira_manager_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_permissoes")
public class Permissao {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é um campo obrigatório")
    private String nome;

    public Permissao() {
    }

    public Permissao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Permissao(String nome) {
        this.nome = nome;
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
}
