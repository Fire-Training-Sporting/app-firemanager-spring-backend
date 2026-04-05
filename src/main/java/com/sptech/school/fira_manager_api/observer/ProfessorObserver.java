package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.service.EmailService;

public class ProfessorObserver implements Observer {

    private final Long id;
    private final EmailService emailService;

    public ProfessorObserver(Long id, EmailService emailService) {
        this.id = id;
        this.emailService = emailService;
    }

    // Este metodo vai receber um id no agendamento, e verificar
    // se é o mesmo professor do observer (aqui), para notificar somente o
    // professor envolvido no agendamento.
    @Override
    public void update(Agendamento agendamento) {
        if (agendamento.getProfessor().getId().equals(id)) {
            String destinatario = agendamento.getProfessor().getEmail();
            String assunto = "Nova Aula Agendada!";
            String mensagem = "Olá! Você tem uma nova aula agendada com aluno(a): " + agendamento.getAluno().getNome() + " - " + agendamento.getAluno().getTelefone() + " no dia " + agendamento.getData() + " às " + agendamento.getHoraInicio() + " no " + agendamento.getCondominio().getNome();

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
