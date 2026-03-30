package com.sptech.school.fira_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para manipulação de saldo do aluno")
public class SaldoDTO {

    @Schema(description = "ID do aluno", example = "1", required = true)
    private Long alunoId;

    @Schema(description = "Quantidade de saldo", example = "10", required = true)
    private Integer quantidade;

    @Schema(description = "Serviço relacionado ao saldo")
    private ServicoDTO servico;

    public SaldoDTO() {
    }

    public SaldoDTO(Long alunoId, Integer quantidade, ServicoDTO servico) {
        this.alunoId = alunoId;
        this.quantidade = quantidade;
        this.servico = servico;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ServicoDTO getServico() {
        return servico;
    }

    public void setServico(ServicoDTO servico) {
        this.servico = servico;
    }
}