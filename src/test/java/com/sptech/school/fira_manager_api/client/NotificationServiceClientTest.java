package com.sptech.school.fira_manager_api.client;

import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailRecuperacaoSenhaNotification;
import com.sptech.school.fira_manager_api.rabbitmq.configuration.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceClientTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void devePublicarRecuperacaoSenhaNaRoutingKeyEspecifica() {
        NotificationServiceClient client = new NotificationServiceClient(rabbitTemplate);
        EmailRecuperacaoSenhaNotification notificacao =
                new EmailRecuperacaoSenhaNotification(
                        "user@example.com",
                        "https://app.example.com/reset-password?token=abc123",
                        30);

        client.notificarRecuperacaoSenha(notificacao);

        verify(rabbitTemplate).convertAndSend(
                RabbitMQConfig.EXCHANGE_NOTIFICACAO,
                RabbitMQConfig.ROUTING_KEY_RECUPERACAO_SENHA,
                notificacao);
    }
}
