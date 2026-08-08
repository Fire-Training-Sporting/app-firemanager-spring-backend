package com.sptech.school.fira_manager_api.mapper.tipoUsuario;

import com.sptech.school.fira_manager_api.dto.responses.tipoUsuario.TipoUsuarioResponse;
import com.sptech.school.fira_manager_api.model.TipoUsuario;

public class TipoUsuarioMapper {

    private TipoUsuarioMapper() {}

    public static TipoUsuarioResponse toResponse(TipoUsuario tipoUsuario) {
        return new TipoUsuarioResponse(tipoUsuario.getId(), tipoUsuario.getCargo());
    }
}