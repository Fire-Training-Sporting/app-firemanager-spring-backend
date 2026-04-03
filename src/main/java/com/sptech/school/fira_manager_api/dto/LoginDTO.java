package com.sptech.school.fira_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginDTO", description = "DTO usado para autenticação de usuários")
public class LoginDTO {

    @Schema(description = "E-Mail do usuário", example = "marcos.vinicius@exemplo.com", required = true)
    @NotBlank(message = "E-Mail é obrigatório")
    @Email(message = "E-Mail inválido")
    private String email;

    @Schema(description = "Senha do usuário", example = "xpto123!", required = true)
    @NotBlank(message = "Senha é obrigatório")
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}