package com.sptech.school.fira_manager_api.dto.requests.saldo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(name = "SaldoRequest", description = "Payload para criar ou atualizar um Saldo de aluno.")
public class SaldoRequest {

    @Schema(description = "ID do aluno", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Aluno é obrigatório")
    private Long aluno;

    @Schema(description = "Quantidade de saldo (aulas)", example = "10.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantidade deve ser maior que zero")
    private Double quantidade;

    @Schema(description = "ID do serviço", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Serviço é obrigatório")
    private Long servico;

    public Long getAluno() {
        return aluno;
    }

    public void setAluno(Long aluno) {
        this.aluno = aluno;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Long getServico() {
        return servico;
    }

    public void setServico(Long servico) {
        this.servico = servico;
    }
}
