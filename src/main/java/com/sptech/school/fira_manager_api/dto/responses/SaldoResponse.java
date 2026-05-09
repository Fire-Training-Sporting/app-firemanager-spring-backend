package com.sptech.school.fira_manager_api.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Schema(name = "SaldoResponse", description = "DTO de resposta do Saldo de um aluno em um serviço.")
public class SaldoResponse {

    @Schema(description = "ID do Saldo", example = "1", required = false)
    private Long id;

    @Schema(description = "Aluno associado ao saldo", required = false)
    private UsuarioResponse aluno;

    @Schema(description = "Quantidade de saldo disponível", example = "10", required = true)
    private Integer quantidade;

    @Schema(description = "Serviço relacionado ao saldo", required = true)
    private ServicoResponse servico;

    public SaldoResponse(Integer quantidade, ServicoResponse servico) {
        this.quantidade = quantidade;
        this.servico = servico;
    }

    public SaldoResponse(Long id, UsuarioResponse aluno, Integer quantidade, ServicoResponse servico) {
        this.id = id;
        this.aluno = aluno;
        this.quantidade = quantidade;
        this.servico = servico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ServicoResponse getServico() {
        return servico;
    }

    public void setServico(ServicoResponse servico) {
        this.servico = servico;
    }

    public Long getId() {
        return id;
    }

    public UsuarioResponse getAluno() {
        return aluno;
    }
}
