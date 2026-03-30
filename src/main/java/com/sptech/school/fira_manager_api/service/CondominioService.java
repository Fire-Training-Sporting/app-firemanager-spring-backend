package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.CondominioDTO;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CondominioService {

    private final CondominioRepository condominioRepository;

    public CondominioService(CondominioRepository condominioRepository) {
        this.condominioRepository = condominioRepository;
    }


    public Condominio adicionarNovoCondominio(CondominioDTO dto) {
        Condominio condominioNovo = new Condominio(dto.getCidade(), dto.getBairro(), dto.getRua(), dto.getNumero());
        return condominioRepository.save(condominioNovo);
    }

    public List<Condominio> obterCondominios() {
        List<Condominio> condominios = condominioRepository.findAll();

        return condominios;
    }

    public Condominio atualizarCondominio(Long id, CondominioDTO dto) {
        Condominio condominioNovo = condominioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O Condominio solicitado não existe."));

        condominioNovo.setCidade(dto.getCidade());
        condominioNovo.setBairro(dto.getBairro());
        condominioNovo.setRua(dto.getRua());
        condominioNovo.setNumero(dto.getNumero());

        return condominioRepository.save(condominioNovo);

    }

    public void deletarCondominio(Long id) {
        if (!condominioRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O Condominio não existe");

        condominioRepository.deleteById(id);
    }
}
