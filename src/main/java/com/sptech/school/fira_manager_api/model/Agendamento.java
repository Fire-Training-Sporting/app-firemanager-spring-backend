package com.sptech.school.fira_manager_api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_aluno", nullable = false)
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "fk_professor", nullable = false)
    private Usuario professor;

    @ManyToOne
    @JoinColumn(name = "fk_auxiliar", nullable = true)
    private Usuario auxiliar;

    @ManyToOne
    @JoinColumn(name = "fk_servico", nullable = false)
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "fk_condominio", nullable = false)
    private Condominio condominio;

    @NotNull(message = "Data não pode estar nula")
    @Column(name = "data_agendamento", nullable = false)
    private LocalDate data;


    @NotNull(message = "Hora de início não pode estar nula")
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim não pode estar nula")
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;


    @Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
    @Column(name = "observacao", length = 255)
    private String observacao;

    @Column(name = "status", nullable = false)
    private String status = "pendente";
    
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = "pendente";
        }
    }

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;


    public Agendamento() {
    }

    public Agendamento(Long id, Usuario aluno, Usuario professor, Usuario auxiliar, Servico servico, Condominio condominio, LocalDate data, LocalTime horaInicio,LocalTime horaFim, String observacao, String status, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.aluno = aluno;
        this.professor = professor;
        this.auxiliar = auxiliar;
        this.servico = servico;
        this.condominio = condominio;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.observacao = observacao;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Agendamento(Usuario aluno, Usuario professor, Usuario auxiliar, Servico servico, Condominio condominio, LocalDate data, LocalTime horaInicio,LocalTime horaFim, String observacao) {
        this.aluno = aluno;
        this.professor = professor;
        this.auxiliar = auxiliar;
        this.servico = servico;
        this.condominio = condominio;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public Usuario getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Usuario auxiliar) {
        this.auxiliar = auxiliar;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}