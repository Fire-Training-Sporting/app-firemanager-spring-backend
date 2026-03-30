package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository <Agendamento, Long> {
}

