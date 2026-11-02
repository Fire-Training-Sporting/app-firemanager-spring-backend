package com.sptech.school.fira_manager_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sptech.school.fira_manager_api.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
   Page<Agendamento> findAllByStatus(String status, Pageable pageable);
   Long countByProfessorIdAndStatus(Long id, String status);
   Long countByAuxiliarIdAndStatus(Long id, String status);
   Long countByProfessorIdAndServicoIdAndStatus(Long professorId, Long servicoId, String status);
   Long countByAuxiliarIdAndServicoIdAndStatus(Long auxiliarId, Long servicoId, String status);
   List<Agendamento> findByStatusIn(List<String> status);
   List<Agendamento> findByProfessorIdAndDataAndStatusNot(Long professorId, LocalDate data, String status);

   @Query("""
           SELECT COUNT(DISTINCT a) FROM Agendamento a
           LEFT JOIN a.alunos alunoGrupo
           WHERE (a.aluno.id = :usuarioId
                  OR a.professor.id = :usuarioId
                  OR a.auxiliar.id = :usuarioId
                  OR a.rebatedor.id = :usuarioId
                  OR alunoGrupo.id = :usuarioId)
             AND a.data >= :dataInicial
             AND a.status IN :status
           """)
   long contarAgendamentosFuturosDoUsuario(@Param("usuarioId") Long usuarioId,
                                           @Param("dataInicial") LocalDate dataInicial,
                                           @Param("status") List<String> status);

   @Query("""
           SELECT COUNT(DISTINCT a) > 0 FROM Agendamento a
           LEFT JOIN a.alunos alunoGrupo
           WHERE a.aluno.id = :usuarioId
              OR a.professor.id = :usuarioId
              OR a.auxiliar.id = :usuarioId
              OR a.rebatedor.id = :usuarioId
              OR alunoGrupo.id = :usuarioId
           """)
   boolean existeAgendamentoDoUsuario(@Param("usuarioId") Long usuarioId);
}
