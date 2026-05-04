package com.sptech.school.fira_manager_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    // MimeMessage é uma classe do framework que representa uma mensagem de email permitindo anexos e HTML
    // tendo como parametro obrigatorio o destinatario, assunto e a mensagem.
    @Async
    public void enviarEmail(String destinatario, String assunto, String texto) {
        try {
            MimeMessage mensagem = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setFrom("");
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(texto, true);

            emailSender.send(mensagem);
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar email para " + destinatario + ": " + e.getMessage());
        }
    }
}
