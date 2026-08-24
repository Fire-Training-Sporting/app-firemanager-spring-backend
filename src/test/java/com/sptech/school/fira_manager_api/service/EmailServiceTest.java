package com.sptech.school.fira_manager_api.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mail.javamail.JavaMailSender;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                emailService,
                "remetente",
                "teste@email.com"
        );
    }

    @Test
    void deveEnviarEmailComSucesso() {
        MimeMessage mimeMessage =
                new MimeMessage(Session.getDefaultInstance(new Properties()));

        when(emailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        emailService.enviarEmail(
                "destino@email.com",
                "Assunto Teste",
                "<h1>Mensagem</h1>"
        );

        verify(emailSender, times(1))
                .createMimeMessage();

        verify(emailSender, times(1))
                .send(mimeMessage);
    }

    @Test
    void deveEntrarNoCatchQuandoEmailForInvalido() {

        MimeMessage mimeMessage =
                new MimeMessage(Session.getDefaultInstance(new Properties()));

        when(emailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        assertDoesNotThrow(() ->
                emailService.enviarEmail(
                        "email invalido",
                        "Assunto",
                        "Mensagem"
                )
        );

        verify(emailSender).createMimeMessage();
    }
}