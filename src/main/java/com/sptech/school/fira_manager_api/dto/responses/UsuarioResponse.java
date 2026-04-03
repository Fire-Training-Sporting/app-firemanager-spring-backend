package com.sptech.school.fira_manager_api.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sptech.school.fira_manager_api.dto.CondominioDTO;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "UsuarioResponse", description = "Representação de um usuário retornado pela API")
public class UsuarioResponse {

    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @Schema(description = "Tipo do usuário", example = "{ \"id\": 4, \"cargo\": \"aluno\" }")
    private TipoUsuario tipoUsuario;

    @Schema(description = "Nome completo do usuário", example = "Marcos Vinicius Albano")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "marcos.vinicius@email.com")
    private String email;

    @Schema(description = "Telefone do usuário (11 dígitos, apenas números)", example = "11948573097")
    private String telefone;

    @Schema(description = "Condomínio em que o aluno recebe aulas. Null se não for aluno")
    private CondominioDTO condominio;

    @Schema(description = "Data de criação do usuário", example = "03/04/2026 11:43:49")
    private LocalDateTime criadoEm;

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

    public CondominioDTO getCondominio() {
        return condominio;
    }

    public void setCondominio(CondominioDTO condominio) {
        this.condominio = condominio;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}