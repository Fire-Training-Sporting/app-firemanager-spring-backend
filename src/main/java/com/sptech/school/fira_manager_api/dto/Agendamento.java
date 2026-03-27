package com.sptech.school.fira_manager_api.dto;

import com.sptech.school.fira_manager_api.model.Aluno;
import com.sptech.school.fira_manager_api.model.Local;
import com.sptech.school.fira_manager_api.model.Professor;
import com.sptech.school.fira_manager_api.model.Saldo;

import java.time.LocalDateTime;

public class Agendamento {

    private Long id;
    private Long alunoId;
    private Long professorId;
    private Long localId;
    private Long servicoId;
    private LocalDateTime data;
    private String observacao;

    public Agendamento() {
    }

    public Agendamento(Long id, Long alunoId, Long professorId,
                          Long localId, Long servicoId,
                          LocalDateTime data, String observacao) {
        this.id = id;
        this.alunoId = alunoId;
        this.professorId = professorId;
        this.localId = localId;
        this.servicoId = servicoId;
        this.data = data;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}