package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.PermissaoDTO;
import com.sptech.school.fira_manager_api.model.Permissao;
import com.sptech.school.fira_manager_api.repository.PermissaoRepository;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }


    public Permissao adicionarNovaPermissao(PermissaoDTO dto) {
        Permissao novaPermissao = new Permissao(dto.getNome());
        return permissaoRepository.save(novaPermissao);
    }

    public List<Permissao> obterPermissoes() {
        List<Permissao> permissoes = permissaoRepository.findAll();

        return permissoes;
    }

    public Permissao atualizarPermissao(Long id, PermissaoDTO dto) {
        Permissao novaPermissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permissão solicitada não encontrada."));

        novaPermissao.setNome(dto.getNome());

        return permissaoRepository.save(novaPermissao);
    }

    public void deletarPermissao(Long id) {
        if (!permissaoRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A Permissão não existe");

        permissaoRepository.deleteById(id);
    }
}