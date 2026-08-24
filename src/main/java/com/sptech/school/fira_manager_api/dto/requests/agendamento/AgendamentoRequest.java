package com.sptech.school.fira_manager_api.dto.requests.agendamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.sptech.school.fira_manager_api.model.TipoAgendamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AgendamentoRequest", description = "Payload para criação e atualização de agendamentos.")
public class AgendamentoRequest {

    @Schema(description = "ID do aluno (obrigatório para tipo INDIVIDUAL)", example = "1")
    private Long aluno;

    @Schema(description = "IDs dos alunos (obrigatório para tipo GRUPO)", example = "[1, 2, 3]")
    private List<Long> alunos;

    @Schema(description = "Tipo do agendamento (INDIVIDUAL ou GRUPO)", example = "INDIVIDUAL")
    private TipoAgendamento tipo;



    @Schema(description = "ID do professor", example = "2")
    @NotNull(message = "ID do professor é obrigatório")
    private Long professor;

    @Schema(description = "ID do auxiliar (opcional)", example = "3")
    private Long auxiliar;

    @Schema(description = "ID do rebatedor (opcional)", example = "4")
    private Long rebatedor;

    @Schema(description = "ID do serviço", example = "5")
    @NotNull(message = "ID do serviço é obrigatório")
    private Long servico;

    @Schema(description = "ID do condomínio", example = "4")
    @NotNull(message = "ID do condomínio é obrigatório")
    private Long condominio;

    @Schema(description = "Data do agendamento", example = "2026-04-10")
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @Schema(description = "Hora de início do agendamento", example = "14:30:00")
    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @Schema(description = "Hora de fim do agendamento", example = "15:30:00")
    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    @Schema(description = "Observações adicionais", example = "Aluno prefere atendimento rápido")
    private String observacao;

    public Long getAluno() {
        return aluno;
    }

    public void setAluno(Long aluno) {
        this.aluno = aluno;
    }

    public List<Long> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Long> alunos) {
        this.alunos = alunos;
    }

    public TipoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendamento tipo) {
        this.tipo = tipo;
    }

    public Long getProfessor() {
        return professor;
    }

    public void setProfessor(Long professor) {
        this.professor = professor;
    }

    public Long getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Long auxiliar) {
        this.auxiliar = auxiliar;
    }

    public Long getRebatedor() {
        return rebatedor;
    }

    public void setRebatedor(Long rebatedor) {
        this.rebatedor = rebatedor;
    }

    public Long getServico() {
        return servico;
    }

    public void setServico(Long servico) {
        this.servico = servico;
    }

    public Long getCondominio() {
        return condominio;
    }

    public void setCondominio(Long condominio) {
        this.condominio = condominio;
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

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
