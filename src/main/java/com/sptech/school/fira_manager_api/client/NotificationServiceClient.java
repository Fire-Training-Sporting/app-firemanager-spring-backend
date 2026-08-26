package com.sptech.school.fira_manager_api.client;

import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailAlunoNotification;
import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailProfessorNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificationServiceClient {

    private final RestClient client;

    public NotificationServiceClient(@Value("${notification-service.url}") String baseUrl) {
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void notificarAluno(EmailAlunoNotification request) {

        client.post()
                .uri("/api/notificacao/email/aluno")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void notificarProfessor(EmailProfessorNotification request) {

        client.post()
                .uri("/api/notificacao/email/professor")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
