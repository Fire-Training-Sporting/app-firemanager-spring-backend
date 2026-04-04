package com.sptech.school.fira_manager_api.dto.responses;

import com.sptech.school.fira_manager_api.model.TipoUsuario;

import java.time.LocalDateTime;

public class ProfessorResponse {


    private Long id;
    private TipoUsuario tipoUsuario;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime criadoEm;

    public ProfessorResponse(Long id, TipoUsuario tipoUsuario, String nome, String email, String telefone, LocalDateTime criadoEm) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
