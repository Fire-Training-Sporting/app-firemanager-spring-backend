package com.sptech.school.fira_manager_api.client;

import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailAlunoNotification;
import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailProfessorNotification;
import com.sptech.school.fira_manager_api.rabbitmq.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceClient {

    private final RabbitTemplate rabbitTemplate;

    public NotificationServiceClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notificarAluno(EmailAlunoNotification request) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_NOTIFICACAO_ALUNO, request);
    }

    public void notificarProfessor(EmailProfessorNotification request) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_NOTIFICACAO_PROFESSOR, request);
    }
}