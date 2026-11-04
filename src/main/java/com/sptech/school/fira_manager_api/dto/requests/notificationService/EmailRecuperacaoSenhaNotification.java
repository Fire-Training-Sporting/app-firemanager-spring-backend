package com.sptech.school.fira_manager_api.dto.requests.notificationService;

public record EmailRecuperacaoSenhaNotification(
        String emailDestinatario,
        String linkRedefinicao,
        int validadeMinutos
) {}
