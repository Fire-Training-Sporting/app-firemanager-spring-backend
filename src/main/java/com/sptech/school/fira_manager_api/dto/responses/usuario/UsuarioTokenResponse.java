package com.sptech.school.fira_manager_api.dto.responses.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioTokenResponse", description = "Dados retornados após autenticação bem-sucedida.")
public class UsuarioTokenResponse {

    @Schema(description = "ID do usuário autenticado", example = "1")
    private Long userId;

    @Schema(description = "Nome do usuário", example = "Marcos Vinicius")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "marcos@email.com")
    private String email;

    @Schema(description = "Cargo/tipo do usuário", example = "professor")
    private String cargo;

    @Schema(description = "Token JWT gerado para a sessão")
    private String token;

    public UsuarioTokenResponse(Long userId, String nome, String email, String cargo, String token) {
        this.userId = userId;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
