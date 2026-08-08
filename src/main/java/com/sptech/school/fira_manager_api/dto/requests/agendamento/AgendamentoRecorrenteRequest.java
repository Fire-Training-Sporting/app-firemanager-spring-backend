package com.sptech.school.fira_manager_api.dto.requests.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AgendamentoRecorrenteRequest", description = "Payload para criação de agendamentos recorrentes.")
public class AgendamentoRecorrenteRequest extends AgendamentoRequest {

    @Schema(description = "Quantidade de agendamentos recorrentes a serem criados", example = "3")
    @NotNull(message = "A quantidade de recorrências é obrigatória")
    @Min(value = 1, message = "A quantidade de recorrências deve ser maior que zero")
    private Integer quantidadeRecorrencias;

    public Integer getQuantidadeRecorrencias() {
        return quantidadeRecorrencias;
    }

    public void setQuantidadeRecorrencias(Integer quantidadeRecorrencias) {
        this.quantidadeRecorrencias = quantidadeRecorrencias;
    }
}
