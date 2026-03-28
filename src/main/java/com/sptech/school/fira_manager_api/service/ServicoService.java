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

    public Servico criarServico(Servico servico) {
        return servicoRepository.findByNome("Luan");
    }

    public List<Servico> buscarServicos() {
        return Collections.singletonList(servicoRepository.findById(2L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public ServicoDTO atualizarServico(Long id, ServicoDTO novoNome) {

        for (int i = 0; i < servicos.size(); i++) {

            if (id.equals(servicos.get(i).getId())) {
                servicos.get(i).setNome(novoNome.getNome());
                return servicos.get(i);
            }
        }

        return null;
    }

    public Boolean deletarServico(Long id) {

        for (int i = 0; i < servicos.size(); i++) {

            if (servicos.get(i).getId() == id) {
                servicos.remove(servicos.get(i));
                return true;
            }
        }

        return false;
    }
}
