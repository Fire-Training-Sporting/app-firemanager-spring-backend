package com.sptech.school.fira_manager_api.dto.responses.agendamento;

import com.sptech.school.fira_manager_api.dto.responses.PaginaResponse;

public record HistoricoAulasResponse(
        PaginaResponse<AgendamentoResponse> pagina,
        long aulasComoProfessor,
        long aulasComoRebatedor,
        long aulasComoAuxiliar
) {
}
