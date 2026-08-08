package com.sptech.school.fira_manager_api.dto.requests.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ServicoRequest", description = "Payload para criar ou atualizar um Serviço.")
public class ServicoRequest {

    @Schema(description = "Nome do serviço", example = "Tênis", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Schema(description = "Disponibilidade do serviço: 1 = ativo, 0 = inativo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Ativo é obrigatório")
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
