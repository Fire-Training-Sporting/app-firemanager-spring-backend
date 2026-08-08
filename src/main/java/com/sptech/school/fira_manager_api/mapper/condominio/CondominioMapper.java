package com.sptech.school.fira_manager_api.mapper.condominio;

import com.sptech.school.fira_manager_api.dto.requests.condominio.CondominioRequest;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.model.Condominio;

public class CondominioMapper {

    private CondominioMapper() {}

    public static Condominio toEntity(CondominioRequest dto) {
        return new Condominio(
                dto.getNome(),
                dto.getCidade(),
                dto.getBairro(),
                dto.getLogradouro(),
                dto.getNumero(),
                dto.getCep()
        );
    }

    public static CondominioResponse toResponse(Condominio condominio) {
        return new CondominioResponse(
                condominio.getId(),
                condominio.getNome(),
                condominio.getCidade(),
                condominio.getBairro(),
                condominio.getLogradouro(),
                condominio.getNumero(),
                condominio.getCep()
        );
    }
}