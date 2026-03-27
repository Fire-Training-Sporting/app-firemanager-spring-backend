package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.Permissao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PermissaoService {

    private final List<Permissao> permissoes = new ArrayList<>();

    public Boolean adicionarNovaPermissao(Integer id, String nome) {
        Permissao novaPermissao = new Permissao(id, nome);
        permissoes.add(novaPermissao);

        return true;
    }

    public List<Permissao> obterPermissoes() {
        return permissoes;
    }

    public Boolean atualizarPermissao(Integer id, String nome) {
        for (int i = 0; i < permissoes.size(); i++) {
            Permissao permissaoAtual = permissoes.get(i);

            if (Objects.equals(permissaoAtual.getId(), id)) {
                permissoes.set(i, new Permissao(id, nome));
                return true;
            }
        }

        return false;
    }

    public Boolean deletarPermissao(Integer id) {
        for (int i = 0; i < permissoes.size(); i++) {
            if (Objects.equals(permissoes.get(i).getId(), id)) {
                permissoes.remove(i);
                return true;
            }
        }

        return false;
    }
}