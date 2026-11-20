package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.mapper.servico.ServicoMapper;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Cacheable(cacheNames = "servicos", key = "'todos'")
    public List<ServicoResponse> buscarServicos() {
        return servicoRepository.findAll()
                .stream()
                .map(ServicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "servicos", key = "#id")
    public ServicoResponse buscarServicoPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe"));
        return ServicoMapper.toResponse(servico);
    }

    @CacheEvict(cacheNames = "servicos", allEntries = true)
    public ServicoResponse atualizarAtividadePorId(Long id, Integer ativo) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe"));

        servico.setAtivo(ativo == 1);

        return ServicoMapper.toResponse(servicoRepository.save(servico));
    }

    @CacheEvict(cacheNames = "servicos", allEntries = true)
    public void deletarServicoPorId(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe");
        }
        servicoRepository.deleteById(id);
    }
}
