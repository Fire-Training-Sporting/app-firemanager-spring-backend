package com.sptech.school.fira_manager_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Aluno é obrigatório")
    @ManyToOne
    @JoinColumn(name = "fk_aluno", nullable = false)
    private Usuario aluno;

    @NotNull(message = "Professor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "fk_professor", nullable = false)
    private Usuario professor;

    @ManyToOne
    @JoinColumn(name = "fk_auxiliar")
    private Usuario auxiliar;

    @NotNull(message = "Condomínio é obrigatório")
    @ManyToOne
    @JoinColumn(name = "fk_condominio", nullable = false)
    private Condominio condominio;

    @NotNull(message = "Serviço é obrigatório")
    @ManyToOne
    @JoinColumn(name = "fk_servico", nullable = false)
    private Servico servico;

    @NotNull(message = "Data não pode estar nula")
    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime data;

    @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String observacao;

    public Agendamento() {
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

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
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