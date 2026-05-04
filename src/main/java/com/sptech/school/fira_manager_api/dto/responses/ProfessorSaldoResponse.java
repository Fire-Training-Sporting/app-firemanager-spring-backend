package com.sptech.school.fira_manager_api.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Schema(name = "ProfessorSaldoResponse", description = "DTO de resposta com o total de aulas concluídas de um professor.")
public class ProfessorSaldoResponse {
        @Schema(description = "Dados do professor")
        private ProfessorResponse professor;

        @Schema(description = "Total de aulas como professor", example = "2")
        private Integer aulasComoProfessor;

        @Schema(description = "Total de aulas como auxiliar", example = "1")
        private Integer aulasComoAuxiliar;

        @Schema(description = "Total geral de aulas", example = "3")
        private Integer totalAulas;

        private List<ServicoProfessorResponse> servicos;

    public ProfessorSaldoResponse(ProfessorResponse professor, List<ServicoProfessorResponse> servicos, Integer aulasComoProfessor, Integer aulasComoAuxiliar) {
        this.professor = professor;
        this.servicos = servicos;
        this.aulasComoProfessor = aulasComoProfessor;
        this.aulasComoAuxiliar = aulasComoAuxiliar;
        this.totalAulas = aulasComoProfessor + aulasComoAuxiliar;
    }

    public void setProfessor(ProfessorResponse professor) {
        this.professor = professor;
    }

    public void setAulasComoProfessor(Integer aulasComoProfessor) {
        this.aulasComoProfessor = aulasComoProfessor;
    }

    public void setAulasComoAuxiliar(Integer aulasComoAuxiliar) {
        this.aulasComoAuxiliar = aulasComoAuxiliar;
    }

    public void setTotalAulas(Integer totalAulas) {
        this.totalAulas = totalAulas;
    }

    public List<ServicoProfessorResponse> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoProfessorResponse> servicos) {
        this.servicos = servicos;
    }

    public ProfessorResponse getProfessor() {
            return professor;
        }

        public Integer getAulasComoProfessor() {
            return aulasComoProfessor;
        }

        public Integer getAulasComoAuxiliar() {
            return aulasComoAuxiliar;
        }

        public Integer getTotalAulas() {
            return totalAulas;
        }
    }

