package com.sptech.school.fira_manager_api.dto;

public class ServicoDTO {

    private String nome;

    public ServicoDTO() {
    }

    public ServicoDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
