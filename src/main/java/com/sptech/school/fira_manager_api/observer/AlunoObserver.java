package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.dto.Agendamento;
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
        if (agendamento.getAlunoId().equals(id)) {
//            String destinatario = agendamento.getAlunoEmail();
//            String assunto = "Nova Aula Agendada!";
//            String mensagem = "Olá! Seu agendamento foi marcado para " + agendamento.getData();
//            emailService.enviarEmail(destinatario, assunto, mensagem);
            System.out.println("Notificação para Aluno ID " + id +
                ": seu agendamento foi realizado para " + agendamento.getData());
        }
    }
}
