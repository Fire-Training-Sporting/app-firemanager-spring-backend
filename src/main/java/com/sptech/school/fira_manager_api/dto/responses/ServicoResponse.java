package com.sptech.school.fira_manager_api.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicoResponse {

    @Schema(description = "ID do Servico", example = "1")
    private Long id;

    @Schema(description = "Nome do serviço", example = "Tênis")
    private String nome;

    public ServicoResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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
}
