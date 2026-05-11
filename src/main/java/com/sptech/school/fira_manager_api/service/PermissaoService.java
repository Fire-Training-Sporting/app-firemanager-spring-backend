package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.permissao.PermissaoRequest;
import com.sptech.school.fira_manager_api.dto.responses.permissao.PermissaoResponse;
import com.sptech.school.fira_manager_api.model.Permissao;
import com.sptech.school.fira_manager_api.repository.PermissaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    private PermissaoResponse toResponse(Permissao permissao) {
        return new PermissaoResponse(permissao.getId(), permissao.getNome());
    }

    public PermissaoResponse adicionarNovaPermissao(PermissaoRequest dto) {
        Permissao novaPermissao = new Permissao(dto.getNome());
        return toResponse(permissaoRepository.save(novaPermissao));
    }

    public List<PermissaoResponse> obterPermissoes() {
        return permissaoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PermissaoResponse atualizarPermissao(Long id, PermissaoRequest dto) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permissão solicitada não encontrada."));

        permissao.setNome(dto.getNome());

        return toResponse(permissaoRepository.save(permissao));
    }

    public void deletarPermissao(Long id) {
        if (!permissaoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A Permissão não existe");
        }
        permissaoRepository.deleteById(id);
    }
}