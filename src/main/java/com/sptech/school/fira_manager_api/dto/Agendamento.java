package com.sptech.school.fira_manager_api.dto;

import com.sptech.school.fira_manager_api.model.*;

import java.time.LocalDateTime;

public class Agendamento {

    private Integer id;
    private Aluno aluno;
    private Professor professor;
    private LocalDateTime data;
    private Local local;
    private Saldo saldo;
    private Servico servico;
    private String observacao;

    public Agendamento() {
    }

    public Agendamento(Integer id, Aluno aluno, Professor professor, LocalDateTime data, Local local, Saldo saldo, Servico servico, String observacao) {
        this.id = id;
        this.aluno = aluno;
        this.professor = professor;
        this.data = data;
        this.local = local;
        this.saldo = saldo;
        this.servico = servico;
        this.observacao = observacao;
    }

    public Agendamento(Aluno aluno, Professor professor, LocalDateTime data, Local local, Saldo saldo, Servico servico, String observacao) {
        this.aluno = aluno;
        this.professor = professor;
        this.data = data;
        this.local = local;
        this.saldo = saldo;
        this.servico = servico;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
