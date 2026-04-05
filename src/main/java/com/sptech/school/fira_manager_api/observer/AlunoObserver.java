package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.service.EmailService;

public class AlunoObserver implements Observer {

    private final Long id;
    private final EmailService emailService;

    public AlunoObserver(Long id, EmailService emailService) {
        this.id = id;
        this.emailService = emailService;
    }

    // Este metodo vai receber um id no agendamento, e verificar
    // se é o mesmo aluno do observer (aqui), para notificar somente o
    // aluno envolvido no agendamento.
    @Override
    public void update(Agendamento agendamento) {
        if (agendamento.getAluno().getId().equals(id)) {
            String destinatario = agendamento.getAluno().getEmail();
            String assunto = "Nova Aula Agendada!";
            String mensagem = "Olá! Seu agendamento foi marcado para " + agendamento.getData() + " às " + agendamento.getHoraInicio();

            try {
                emailService.enviarEmail(destinatario, assunto, mensagem);
                System.out.println("Email enviado para: " + destinatario);
            } catch (Exception e) {
                System.err.println("Erro ao enviar email: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}


