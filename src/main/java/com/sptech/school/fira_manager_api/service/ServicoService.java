package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.mapper.servico.ServicoMapper;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public List<ServicoResponse> buscarServicos() {
        return servicoRepository.findAll()
                .stream()
                .map(ServicoMapper::toResponse)
                .toList();
    }

    public ServicoResponse buscarServicoPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe"));
        return ServicoMapper.toResponse(servico);
    }

    public ServicoResponse atualizarAtividadePorId(Long id, Integer ativo) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe"));

        servico.setAtivo(ativo == 1);

        return ServicoMapper.toResponse(servicoRepository.save(servico));
    }

    public void deletarServicoPorId(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe");
        }
        servicoRepository.deleteById(id);
    }
}
