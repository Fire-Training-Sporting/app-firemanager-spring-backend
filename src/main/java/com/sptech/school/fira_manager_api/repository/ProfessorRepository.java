package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
