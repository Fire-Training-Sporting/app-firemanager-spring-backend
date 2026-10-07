package com.sptech.school.fira_manager_api.service;

import java.util.List;

import com.sptech.school.fira_manager_api.mapper.tipoUsuario.TipoUsuarioMapper;
import org.springframework.stereotype.Service;

import com.sptech.school.fira_manager_api.dto.responses.tipoUsuario.TipoUsuarioResponse;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public List<TipoUsuarioResponse> buscarTipoUsuarios() {
        return tipoUsuarioRepository.findAll()
                .stream()
                .filter(tipo -> !tipo.getCargo().equalsIgnoreCase("root"))
                .map(TipoUsuarioMapper::toResponse)
                .toList();
    }
}
