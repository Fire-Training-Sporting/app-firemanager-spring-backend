package com.sptech.school.fira_manager_api.dto;

import com.sptech.school.fira_manager_api.model.ETipoUsuario;

public class Usuario {

    private Long id;
    private ETipoUsuario tipoUsuario;
    private String nome;
    private String email;
    private String numero;
    private String senha;
//    private Condominio condominio


    public Usuario() {
    }

    public Usuario(Long id, ETipoUsuario tipoUsuario, String nome, String email, String numero, String senha) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.email = email;
        this.numero = numero;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ETipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(ETipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
