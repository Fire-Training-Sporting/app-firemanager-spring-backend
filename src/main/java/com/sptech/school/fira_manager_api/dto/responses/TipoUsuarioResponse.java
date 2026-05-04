package com.sptech.school.fira_manager_api.dto.responses;

public class TipoUsuarioResponse {

    private Long id;
    private String cargo;

    public TipoUsuarioResponse() {
    }

    public TipoUsuarioResponse(Long id, String cargo) {
        this.id = id;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
