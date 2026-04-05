package com.sptech.school.fira_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ServicoDTO", description = "DTO usado para criar ou atualizar um serviço")
public class ServicoDTO {

    @Schema(description = "Nome do serviço", example = "Tênis", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
