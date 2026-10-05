package com.sptech.school.fira_manager_api.dto.requests.notificationService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EmailProfessorNotification(
        Long agendamentoId,
        String nomeAluno,
        String telefoneAluno,
        String nomeCondominio,
        String observacao,
        LocalDate data,
        LocalTime hora,
        String status,
        String emailDestinatario
) {}
