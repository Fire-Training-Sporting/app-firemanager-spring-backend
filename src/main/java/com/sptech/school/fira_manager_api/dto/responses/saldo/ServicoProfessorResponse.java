package com.sptech.school.fira_manager_api.dto.responses.saldo;

public class ServicoProfessorResponse {

    private String nome;
    private Integer aulasProfessor;
    private Integer aulasAuxiliar;
    private Integer total;

    public ServicoProfessorResponse(String nome, Integer aulasProfessor, Integer aulasAuxiliar) {
        this.nome = nome;
        this.aulasProfessor = aulasProfessor;
        this.aulasAuxiliar = aulasAuxiliar;
        this.total = aulasProfessor + aulasAuxiliar;
    }

    public String getNome() {
        return nome;
    }

    public Integer getAulasProfessor() {
        return aulasProfessor;
    }

    public Integer getAulasAuxiliar() {
        return aulasAuxiliar;
    }

    public Integer getTotal() {
        return total;
    }
}
