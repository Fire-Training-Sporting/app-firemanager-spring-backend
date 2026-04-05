package com.sptech.school.fira_manager_api.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sptech.school.fira_manager_api.dto.CondominioDTO;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

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
        "status",
        "Saldo"
})

@JsonInclude(JsonInclude.Include.NON_NULL)

@Schema(name = "AgendamentoResponse", description = "DTO de resposta de um Agendamento.")
public class AgendamentoResponse {

    @Schema(description = "ID do Agendamento", example = "1", required = true)
    private Long id;

    @Schema(description = "Aluno do Agendamento", required = true)
    private UsuarioResponse aluno;

    @Schema(description = "Saldo do aluno no serviço", required = true)
    private SaldoResponse saldo;

    @Schema(description = "Professor responsável pelo Agendamento", required = true)
    private ProfessorResponse professor;

    @Schema(description = "Professor auxiliar do Agendamento", required = false)
    private ProfessorResponse auxiliar;

    @Schema(description = "Serviço do Agendamento", required = true)
    private ServicoResponse servico;

    @Schema(description = "Condomínio onde o Agendamento foi realizado", required = true)
    private CondominioResponse condominio;

    @Schema(description = "Data do Agendamento", example = "2026-04-14", required = true)
    private LocalDate data;

    @Schema(description = "Hora de início do Agendamento", example = "10:00:00", required = true)
    private LocalTime horaInicio;

    @Schema(description = "Status do Agendamento", example = "pendente", required = true)
    private String status;

    @Schema(description = "Observação do Agendamento", example = "Aula experimental", required = false)
    private String observacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data de criação do Agendamento", example = "2026-04-05 01:43:30", required = true)
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data da última atualização do Agendamento", example = "2026-04-05 01:43:30", required = true)
    private LocalDateTime atualizadoEm;
    public AgendamentoResponse() {
    }

    public AgendamentoResponse(Long id, UsuarioResponse aluno,SaldoResponse saldo, ProfessorResponse professor, ProfessorResponse auxiliar, ServicoResponse servico, CondominioResponse condominio, LocalDate data, LocalTime horaInicio, String observacao, LocalDateTime criadoEm, LocalDateTime atualizadoEm, String status) {
        this.id = id;
        this.aluno = aluno;
        this.saldo = saldo;
        this.professor = professor;
        this.auxiliar = auxiliar;
        this.servico = servico;
        this.condominio = condominio;
        this.data = data;
        this.horaInicio = horaInicio;
        this.observacao = observacao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.status = status;
    }

    public AgendamentoResponse(Long id, UsuarioResponse aluno,SaldoResponse saldo, ProfessorResponse professor, ServicoResponse servico,CondominioResponse condominio, LocalDate data, LocalTime horaInicio, String observacao, LocalDateTime criadoEm, LocalDateTime atualizadoEm, String status) {
        this.id = id;
        this.aluno = aluno;
        this.saldo = saldo;
        this.professor = professor;
        this.servico = servico;
        this.condominio = condominio;
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

    public UsuarioResponse getAluno() {
        return aluno;
    }

    public void setAluno(UsuarioResponse aluno) {
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

    public SaldoResponse getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoResponse saldo) {
        this.saldo = saldo;
    }

    public CondominioResponse getCondominio() {
        return condominio;
    }

    public void setCondominio(CondominioResponse condominio) {
        this.condominio = condominio;
    }
}
