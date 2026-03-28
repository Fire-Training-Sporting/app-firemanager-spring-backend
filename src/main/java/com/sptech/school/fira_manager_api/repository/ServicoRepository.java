package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    Servico findByNome(String nome);
}
