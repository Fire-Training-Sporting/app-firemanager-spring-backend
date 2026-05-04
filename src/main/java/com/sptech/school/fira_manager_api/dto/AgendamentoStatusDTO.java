package com.sptech.school.fira_manager_api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para atualização de status e soft delete de agendamentos")
public class AgendamentoStatusDTO {
    @Schema(description = "Status do agendamento")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
