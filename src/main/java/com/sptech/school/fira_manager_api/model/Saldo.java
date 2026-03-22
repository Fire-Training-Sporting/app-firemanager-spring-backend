package com.sptech.school.fira_manager_api.model;

import com.sptech.school.fira_manager_api.dto.Servico;

public class Saldo {

    private Integer quanidade;
    private Servico servico;

    public Saldo() {
    }

    public Saldo(int quanidade, Servico servico) {
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
}
