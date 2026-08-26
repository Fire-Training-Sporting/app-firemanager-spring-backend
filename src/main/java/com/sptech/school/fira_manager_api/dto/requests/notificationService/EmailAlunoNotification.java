package com.sptech.school.fira_manager_api.dto.requests.notificationService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EmailAlunoNotification(
        Long agendamentoId,
        String nomeProfessor,
        LocalDate data,
        LocalTime hora,
        String status,
        String emailDestinatario
) {}
