package com.sptech.school.fira_manager_api.dto;

import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de saldo do usuario")
public class SaldoDTO {
    @Schema(description = "Id do aluno", example = "1")
    private Long aluno;

    @Schema(description = "Saldo do usuario", example = "12")
    private Double quantidade;

    @Schema(description = "Aula de tenis", example = "Tenis")
    private Long servico;

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Long getServico() {
        return servico;
    }

    public Long getAluno() {
        return aluno;
    }

    public void setAluno(Long aluno) {
        this.aluno = aluno;
    }

    public void setServico(Long servico) {
        this.servico = servico;
    }
}
