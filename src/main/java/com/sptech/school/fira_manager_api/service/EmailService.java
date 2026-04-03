package com.sptech.school.fira_manager_api.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    // SimpleMailMessage é uma classe do framework que representa uma mensagem de email
    // tendo como parametro obrigatorio o destinatario, assunto e a mensagem.
    public void enviarEmail(String destinatario, String assunto, String texto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(texto);

        emailSender.send(message);
    }
}
