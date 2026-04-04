package com.sptech.school.fira_manager_api.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sptech.school.fira_manager_api.model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@JsonPropertyOrder({
        "id",
        "aluno",
        "professor",
        "auxiliar",
        "servico",
        "data",
        "horaInicio",
        "observacao",
        "criadoEm",
        "atualizadoEm",
        "status"
})
public class AgendamentoResponse {

    private Long id;
    private Usuario aluno;
    private ProfessorResponse professor;
    private ProfessorResponse auxiliar;
    private ServicoResponse servico;
    private LocalDate data;
    private LocalTime horaInicio;
    private String observacao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;
    private String status;

    public AgendamentoResponse() {
    }

    public AgendamentoResponse(Long id, Usuario aluno, ProfessorResponse professor, ProfessorResponse auxiliar, ServicoResponse servico, LocalDate data, LocalTime horaInicio, String observacao, LocalDateTime criadoEm, LocalDateTime atualizadoEm, String status) {
        this.id = id;
        this.aluno = aluno;
        this.professor = professor;
        this.auxiliar = auxiliar;
        this.servico = servico;
        this.data = data;
        this.horaInicio = horaInicio;
        this.observacao = observacao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.status = status;
    }

    public AgendamentoResponse(Long id, Usuario aluno, ProfessorResponse professor, ServicoResponse servico, LocalDate data, LocalTime horaInicio, String observacao, LocalDateTime criadoEm, LocalDateTime atualizadoEm, String status) {
        this.id = id;
        this.aluno = aluno;
        this.professor = professor;
        this.servico = servico;
        this.data = data;
        this.horaInicio = horaInicio;
        this.observacao = observacao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.status = status;
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

    public ProfessorResponse getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorResponse professor) {
        this.professor = professor;
    }

    public ProfessorResponse getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(ProfessorResponse auxiliar) {
        this.auxiliar = auxiliar;
    }

    public ServicoResponse getServico() {
        return servico;
    }

    public void setServico(ServicoResponse servico) {
        this.servico = servico;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
