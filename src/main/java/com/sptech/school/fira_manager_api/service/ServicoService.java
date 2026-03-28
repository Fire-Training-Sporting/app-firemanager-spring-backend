package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.ServicoDTO;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico criarServico(ServicoDTO dto) {
        Servico servicoNovo = new Servico(dto.getNome());
        return servicoRepository.save(servicoNovo);
    }

    public List<Servico> buscarServicos() {
        List<Servico> servicos = servicoRepository.findAll();

        return servicos;
    }

    public Servico buscarServicoPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe"));
    }

    public Servico atualizarServicoPorId(Long id, ServicoDTO dto) {
        Servico servicoNovo = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe"));

        servicoNovo.setNome(dto.getNome());
        return servicoRepository.save(servicoNovo);
    }

    public void deletarServicoPorId(Long id) {
        if (!servicoRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O serviço não existe");

        servicoRepository.deleteById(id);
    }
}
