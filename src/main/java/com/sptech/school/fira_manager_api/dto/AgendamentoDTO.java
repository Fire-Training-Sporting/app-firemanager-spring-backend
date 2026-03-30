package com.sptech.school.fira_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO para criação e atualização de agendamentos")
public class AgendamentoDTO {

    @Schema(description = "ID do aluno", example = "1")
    private Long alunoId;

    @Schema(description = "ID do professor", example = "2")
    private Long professorId;

    @Schema(description = "ID do local", example = "3")
    private Long localId;

    @Schema(description = "ID do serviço", example = "4")
    private Long servicoId;

    @Schema(description = "Data e hora do agendamento", example = "2026-04-10T14:30:00")
    private LocalDateTime data;

    @Schema(description = "Observações adicionais", example = "Aluno prefere atendimento rápido")
    private String observacao;

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(Long alunoId, Long professorId,
                          Long localId, Long servicoId,
                          LocalDateTime data, String observacao) {
        this.alunoId = alunoId;
        this.professorId = professorId;
        this.localId = localId;
        this.servicoId = servicoId;
        this.data = data;
        this.observacao = observacao;
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