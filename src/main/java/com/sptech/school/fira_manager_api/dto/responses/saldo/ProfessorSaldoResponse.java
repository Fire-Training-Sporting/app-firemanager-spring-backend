package com.sptech.school.fira_manager_api.dto.responses.saldo;

import com.sptech.school.fira_manager_api.dto.responses.usuario.ProfessorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ProfessorSaldoResponse", description = "Dados de resposta com o total de aulas concluídas de um professor.")
public class ProfessorSaldoResponse {

    @Schema(description = "Dados do professor")
    private ProfessorResponse professor;

    @Schema(description = "Lista de aulas por serviço")
    private List<ServicoProfessorResponse> servicos;

    @Schema(description = "Total de aulas como professor principal", example = "2")
    private Integer aulasComoProfessor;

    @Schema(description = "Total de aulas como auxiliar", example = "1")
    private Integer aulasComoAuxiliar;

    @Schema(description = "Total geral de aulas", example = "3")
    private Integer totalAulas;

    public ProfessorSaldoResponse(ProfessorResponse professor, List<ServicoProfessorResponse> servicos,
                                   Integer aulasComoProfessor, Integer aulasComoAuxiliar) {
        this.professor = professor;
        this.servicos = servicos;
        this.aulasComoProfessor = aulasComoProfessor;
        this.aulasComoAuxiliar = aulasComoAuxiliar;
        this.totalAulas = aulasComoProfessor + aulasComoAuxiliar;
    }

    public ProfessorResponse getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorResponse professor) {
        this.professor = professor;
    }

    public List<ServicoProfessorResponse> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoProfessorResponse> servicos) {
        this.servicos = servicos;
    }

    public Integer getAulasComoProfessor() {
        return aulasComoProfessor;
    }

    public void setAulasComoProfessor(Integer aulasComoProfessor) {
        this.aulasComoProfessor = aulasComoProfessor;
    }

    public Integer getAulasComoAuxiliar() {
        return aulasComoAuxiliar;
    }

    public void setAulasComoAuxiliar(Integer aulasComoAuxiliar) {
        this.aulasComoAuxiliar = aulasComoAuxiliar;
    }

    public Integer getTotalAulas() {
        return totalAulas;
    }

    public void setTotalAulas(Integer totalAulas) {
        this.totalAulas = totalAulas;
    }
}
