package com.sptech.school.fira_manager_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sptech.school.fira_manager_api.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
   Page<Agendamento> findAllByStatus(String status, Pageable pageable);
   Long countByProfessorIdAndStatus(Long id, String status);
   Long countByAuxiliarIdAndStatus(Long id, String status);
   Long countByProfessorIdAndServicoIdAndStatus(Long professorId, Long servicoId, String status);
   Long countByAuxiliarIdAndServicoIdAndStatus(Long auxiliarId, Long servicoId, String status);
   List<Agendamento> findByStatusIn(List<String> status);
}
