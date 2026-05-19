package com.sptech.school.fira_manager_api.dto.requests.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "AgendamentoStatusRequest", description = "Payload para atualização de status de agendamentos.")
public class AgendamentoStatusRequest {

    @Schema(description = "Novo status do agendamento", example = "cancelado",
            allowableValues = {"confirmado", "cancelado", "finalizado"})
    @NotBlank(message = "Status é obrigatório")
    private String status;

    @Schema(description = "Justificativa obrigatória quando o status for 'cancelado'",
            example = "Cancelado devido a forte chuva e alagamento")
    private String observacao;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
