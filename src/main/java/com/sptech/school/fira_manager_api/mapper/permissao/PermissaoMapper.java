package com.sptech.school.fira_manager_api.mapper.permissao;

import com.sptech.school.fira_manager_api.dto.requests.permissao.PermissaoRequest;
import com.sptech.school.fira_manager_api.dto.responses.permissao.PermissaoResponse;
import com.sptech.school.fira_manager_api.model.Permissao;

public class PermissaoMapper {

    private PermissaoMapper() {}

    public static Permissao toEntity(PermissaoRequest dto) {
        return new Permissao(dto.getNome());
    }

    public static PermissaoResponse toResponse(Permissao permissao) {
        return new PermissaoResponse(permissao.getId(), permissao.getNome());
    }
}
