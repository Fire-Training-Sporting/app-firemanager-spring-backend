package com.sptech.school.fira_manager_api.dto;

public class Servico {

    private Long id;
    private String nome;

    public Servico() {
    }

    public Servico(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
