package com.sptech.school.fira_manager_api.dto.responses.saldo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "SaldoResponse", description = "Dados de resposta do Saldo de um aluno em um serviço.")
public class SaldoResponse {

    @Schema(description = "ID do Saldo", example = "1")
    private Long id;

    @Schema(description = "Aluno associado ao saldo")
    private UsuarioResponse aluno;

    @Schema(description = "Quantidade de saldo disponível", example = "10.0")
    private Double quantidade;

    @Schema(description = "Serviço relacionado ao saldo")
    private ServicoResponse servico;

    public SaldoResponse(Double quantidade, ServicoResponse servico) {
        this.quantidade = quantidade;
        this.servico = servico;
    }

    public SaldoResponse(Long id, UsuarioResponse aluno, Double quantidade, ServicoResponse servico) {
        this.id = id;
        this.aluno = aluno;
        this.quantidade = quantidade;
        this.servico = servico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioResponse getAluno() {
        return aluno;
    }

    public void setAluno(UsuarioResponse aluno) {
        this.aluno = aluno;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public ServicoResponse getServico() {
        return servico;
    }

    public void setServico(ServicoResponse servico) {
        this.servico = servico;
    }
}
