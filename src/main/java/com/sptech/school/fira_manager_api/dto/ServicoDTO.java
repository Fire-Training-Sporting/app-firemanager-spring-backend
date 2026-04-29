package com.sptech.school.fira_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ServicoDTO", description = "DTO usado para criar ou atualizar um serviço")
public class ServicoDTO {

    @Schema(description = "Nome do serviço", example = "Tênis", required = true)
    private String nome;


    @Schema(description = "Disponibilidade do Serviço", example = "0 (false) / 1 (true)", required = true)
    @NotNull(message = "Atividade é obrigatório")
    private Integer ativo;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

}
