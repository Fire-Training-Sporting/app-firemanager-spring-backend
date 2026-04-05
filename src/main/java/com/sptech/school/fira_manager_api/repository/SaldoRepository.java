package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    Optional<Saldo> findByAlunoIdAndServicoId(Long alunoId, Long servicoId);
}
