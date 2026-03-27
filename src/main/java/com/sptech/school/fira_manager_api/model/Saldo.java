package com.sptech.school.fira_manager_api.model;

import com.sptech.school.fira_manager_api.dto.Servico;

public class Saldo {

    private Long alunoID;
    private Integer quanidade;
    private Servico servico;

    public Saldo() {
    }

    public Saldo(Long alunoID, Integer quanidade, Servico servico) {
        this.alunoID = alunoID;
        this.quanidade = quanidade;
        this.servico = servico;
    }

    public int getQuanidade() {
        return quanidade;
    }

    public void setQuanidade(int quanidade) {
        this.quanidade = quanidade;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Long getAlunoID() {
        return alunoID;
    }

    public void setAlunoID(Long alunoID) {
        this.alunoID = alunoID;
    }

    public void setQuanidade(Integer quanidade) {
        this.quanidade = quanidade;
    }
}
