package com.sptech.school.fira_manager_api.dto.responses.servico;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ServicoResponse", description = "Dados de resposta de um Serviço.")
public class ServicoResponse {

    @Schema(description = "ID do Serviço", example = "1")
    private Long id;

    @Schema(description = "Nome do serviço", example = "Tênis")
    private String nome;

    @Schema(description = "Indica se o serviço está ativo", example = "true")
    private Boolean ativo;

    public ServicoResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ServicoResponse(Long id, String nome, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
