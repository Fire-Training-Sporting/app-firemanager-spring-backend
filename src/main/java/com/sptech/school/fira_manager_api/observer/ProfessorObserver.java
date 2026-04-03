package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.dto.Agendamento;
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
        if (agendamento.getProfessorId().equals(id)) {
//            String destinatario = agendamento.getProfessorEmail;
//            String assunto = "Nova Aula Agendada!";
//            String mensagem = "Olá! Você tem uma nova aula agendada com aluno(a) " + agendamento.getAlunoNome() + " no dia " + agendamento.getData() + " no " + agendamento.getCondominioNome();
            System.out.println("Notificação para Professor ID " + id +
                    ": Novo agendamento com Aluno ID " + agendamento.getAlunoId() +
                    " em " + agendamento.getData());
        }
    }
}
