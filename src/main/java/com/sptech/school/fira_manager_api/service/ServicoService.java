package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.Servico;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {

    private final List<Servico> servicos = new ArrayList<>();
    private Long id = -1L;

    public Servico adicionarServico(String servicoNome) {

        id++;

        Servico servico = new Servico(id, servicoNome);
        servicos.add(servico);
        return servico;
    }

    public List<Servico> obterServicos() { return servicos; }

    public Servico atualizarServico(Long id, Servico novoNome) {

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
