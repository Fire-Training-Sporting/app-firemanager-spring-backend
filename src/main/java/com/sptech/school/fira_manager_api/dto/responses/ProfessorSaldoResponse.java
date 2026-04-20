package com.sptech.school.fira_manager_api.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;


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

        public ProfessorSaldoResponse(ProfessorResponse professor, Integer aulasComoProfessor, Integer aulasComoAuxiliar) {
            this.professor = professor;
            this.aulasComoProfessor = aulasComoProfessor;
            this.aulasComoAuxiliar = aulasComoAuxiliar;
            this.totalAulas = aulasComoProfessor + aulasComoAuxiliar;
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

