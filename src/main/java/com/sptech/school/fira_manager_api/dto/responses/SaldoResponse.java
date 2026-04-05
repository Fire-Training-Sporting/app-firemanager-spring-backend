package com.sptech.school.fira_manager_api.dto.responses;

import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;

public class SaldoResponse {

    private Long id;
    private Usuario aluno;

    private  Integer quantidade;

    private Servico servico;

    public SaldoResponse() {
    }

    public SaldoResponse(Long id, Usuario aluno, Integer quantidade, Servico servico) {
        this.id = id;
        this.aluno = aluno;
        this.quantidade = quantidade;
        this.servico = servico;
    }


    public Integer getQuantidade() {
        return quantidade;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}
