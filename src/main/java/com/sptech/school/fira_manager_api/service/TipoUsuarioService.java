package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.responses.TipoUsuarioResponse;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    private TipoUsuarioResponse mapearResponse(TipoUsuario tipoUsuario) {
        return new TipoUsuarioResponse(tipoUsuario.getId(), tipoUsuario.getCargo());
    }

    public List<TipoUsuarioResponse> buscarTipoUsuarios() {
        return tipoUsuarioRepository.findAll()
                .stream()
                .filter(tipo -> !tipo.getCargo().equalsIgnoreCase("root"))
                .map(this::mapearResponse)
                .toList();
    }
}
