package com.sptech.school.fira_manager_api.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PermissaoDTO", description = "DTO usado para criar ou atualizar uma Permissão.")
public class PermissaoResponse {

    @Schema(description = "ID da Permissão", example = "1")
    private Long id;

    @Schema(description = "Nome da permissão", example = "Criar ou Excluir agendamento")
    private String nome;

    public PermissaoResponse(Long id, String nome) {
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
