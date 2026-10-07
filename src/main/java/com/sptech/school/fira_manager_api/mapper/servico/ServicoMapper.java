package com.sptech.school.fira_manager_api.mapper.servico;

import com.sptech.school.fira_manager_api.dto.requests.servico.ServicoRequest;
import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.model.Servico;

public class ServicoMapper {

    private ServicoMapper() {}

    public static Servico toEntity(ServicoRequest dto) {
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setAtivo(dto.getAtivo() == 1);
        return servico;
    }

    public static ServicoResponse toResponse(Servico servico) {
        return new ServicoResponse(servico.getId(), servico.getNome(), servico.getAtivo());
    }
}
