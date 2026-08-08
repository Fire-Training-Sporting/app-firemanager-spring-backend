package com.sptech.school.fira_manager_api.dto.requests.permissao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "PermissaoRequest", description = "Payload para criar ou atualizar uma Permissão.")
public class PermissaoRequest {

    @Schema(description = "Nome da Permissão", example = "Criar agendamento", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
