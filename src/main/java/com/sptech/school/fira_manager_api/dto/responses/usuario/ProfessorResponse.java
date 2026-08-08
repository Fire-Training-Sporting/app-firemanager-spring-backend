package com.sptech.school.fira_manager_api.dto.responses.usuario;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ProfessorResponse", description = "Dados de resposta de um Professor.")
public class ProfessorResponse {

    @Schema(description = "ID do Professor", example = "1")
    private Long id;

    @Schema(description = "Nome do Professor", example = "Luan")
    private String nome;

    @Schema(description = "E-mail do Professor", example = "luan@email.com")
    private String email;

    @Schema(description = "Telefone do Professor", example = "11999999999")
    private String telefone;

    @Schema(description = "Data de criação do Professor", example = "2026-04-05T00:23:00")
    private LocalDateTime criadoEm;

    public ProfessorResponse(Long id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public ProfessorResponse(Long id, String nome, String email, String telefone, LocalDateTime criadoEm) {
        this.id = id;
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
