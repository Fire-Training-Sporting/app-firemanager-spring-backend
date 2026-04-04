package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.dto.responses.AgendamentoResponse;
import com.sptech.school.fira_manager_api.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
   List<Agendamento> findAllByStatus(String status);
}
