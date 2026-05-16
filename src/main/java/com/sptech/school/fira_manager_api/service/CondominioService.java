package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.condominio.CondominioRequest;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.mapper.condominio.CondominioMapper;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CondominioService {

    private final CondominioRepository condominioRepository;

    public CondominioService(CondominioRepository condominioRepository) {
        this.condominioRepository = condominioRepository;
    }

    public CondominioResponse adicionarNovoCondominio(CondominioRequest dto) {
        Condominio condominioNovo = CondominioMapper.toEntity(dto);
        return CondominioMapper.toResponse(condominioRepository.save(condominioNovo));
    }

    public List<CondominioResponse> obterCondominios() {
        return condominioRepository.findAll()
                .stream()
                .map(CondominioMapper::toResponse)
                .toList();
    }

    public CondominioResponse atualizarCondominio(Long id, CondominioRequest dto) {
        Condominio condominio = condominioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O Condominio solicitado não existe."));

        condominio.setNome(dto.getNome());
        condominio.setCidade(dto.getCidade());
        condominio.setBairro(dto.getBairro());
        condominio.setLogradouro(dto.getLogradouro());
        condominio.setNumero(dto.getNumero());
        condominio.setCep(dto.getCep());

        return CondominioMapper.toResponse(condominioRepository.save(condominio));
    }

    public void deletarCondominio(Long id) {
        if (!condominioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O Condominio não existe");
        }
        condominioRepository.deleteById(id);
    }
}
