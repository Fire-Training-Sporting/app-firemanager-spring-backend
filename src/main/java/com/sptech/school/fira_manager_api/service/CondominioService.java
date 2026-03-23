package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.*;
import com.sptech.school.fira_manager_api.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CondominioService {

    private final List<Condominio> condominios = new ArrayList<>();

    public Boolean adicionarNovoCondominio(
            Integer id,
            String cidade,
            String bairro,
            String rua,
            String numero ) {
        Condominio novoCondominio = new Condominio(
                id, // verificar depois
                cidade,
                bairro,
                rua,
                numero
        );
        condominios.add(novoCondominio);

        return true;
    }

    public List<Condominio> obterCondominios() { return condominios; }

    public Boolean atualizarCondominio(
            Integer id,
            String cidade,
            String bairro,
            String rua,
            String numero ) {
        for (int i = 0; i < condominios.size(); i++) {
            Condominio condominioAtual = condominios.get(i);

            if (Objects.equals(condominioAtual.getId(), id)) {
                condominios.set(i, new Condominio(
                        id,
                        cidade,
                        bairro,
                        rua,
                        numero));
                return true;
            }
        }

        return false;
    }

    public Boolean deletarCondominio(Integer id) {
        for (int i = 0; i < condominios.size(); i++) {
            if (Objects.equals(condominios.get(i).getId(), id)) {
                condominios.remove(i);
                return true;
            }
        }

        return false;
    }
}
