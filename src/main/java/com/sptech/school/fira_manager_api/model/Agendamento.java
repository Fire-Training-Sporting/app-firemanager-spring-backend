package com.sptech.school.fira_manager_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agendamentoId;

    @NotNull(message = "Aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "Professor é obrigatório")
    private Long professorId;

    @NotNull(message = "Local é obrigatório")
    private Long localId;

    @NotNull(message = "Serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "Data não pode estar nula")
    private LocalDateTime data;

    @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
    private String observacao;

    public Agendamento() {
    }

    public Agendamento(Long alunoId, Long professorId,
                       Long localId, Long servicoId,
                       LocalDateTime data, String observacao) {
        this.alunoId = alunoId;
        this.professorId = professorId;
        this.localId = localId;
        this.servicoId = servicoId;
        this.data = data;
        this.observacao = observacao;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
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