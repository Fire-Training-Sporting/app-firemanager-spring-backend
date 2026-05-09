package com.sptech.school.fira_manager_api.observer;

import java.time.format.DateTimeFormatter;

import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.service.EmailService;

public class ProfessorObserver implements Observer {

    private final Long id;
    private final EmailService emailService;
    private static final DateTimeFormatter DATA_FORMATADA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA_FORMATADA = DateTimeFormatter.ofPattern("HH:mm");

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

            Long agendamentoId = agendamento.getId();
            String aluno = agendamento.getAluno().getNome();
            String telefoneAluno = agendamento.getAluno().getTelefone();
            String data = agendamento.getData().format(DATA_FORMATADA);
            String hora = agendamento.getHoraInicio().format(HORA_FORMATADA);
            String nomeCondominio = agendamento.getCondominio().getNome();
            String observacao = agendamento.getObservacao() != null ? agendamento.getObservacao() : "-";

            String assunto = "";
            String mensagemStatus = "";

            switch (agendamento.getStatus().toLowerCase()) {
                case "pendente":
                    assunto = "[FireManager] - Nova Aula Agendada!";
                    mensagemStatus = String.format("Você tem uma nova aula agendada com %s no dia %s às %s. [ID Agendamento: %d]",
                            aluno, data, hora, agendamentoId);
                    break;
                case "cancelado":
                    assunto = "[FireManager] - Agendamento Cancelado";
                    mensagemStatus = String.format("O agendamento com %s no dia %s às %s foi cancelado. [ID Agendamento: %d]",
                            aluno, data, hora, agendamentoId);
                    break;
                case "confirmado":
                    assunto = "[FireManager] - Aula Confirmada";
                    mensagemStatus = String.format("A aula com %s é daqui menos de 24h e não pode mais ser cancelada. [ID Agendamento: %d]",
                            aluno, agendamentoId);
                    break;
                case "finalizado":
                    assunto = "[FireManager] - Aula Concluída";
                    mensagemStatus = String.format("A aula com %s realizada em %s às %s foi concluída. [ID Agendamento: %d]",
                            aluno, data, hora, agendamentoId);
                    break;
            }

            String mensagem = """
                    <html>
                      <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;">
                        <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 10px; border-top: 6px solid #ff6600; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    
                          <h2 style="color: #ff6600; text-align: center;">Olá, Professor(a)!</h2>
                    
                          <p style="font-size: 16px; color: #333333; text-align: center;">
                            %s
                          </p>
                          
                          <div style="background-color: #f9f9f9; padding: 15px; border-radius: 8px; margin: 15px 0; text-align: left;">
                            <p><strong>Aluno(a):</strong> %s</p>
                            <p><strong>Telefone:</strong> %s</p>
                            <p><strong>Condomínio:</strong> %s</p>
                            <p><strong>Observação:</strong> %s</p>
                            <p><strong>Data:</strong> %s</p>
                            <p><strong>Hora:</strong> %s</p>
                          </div>
                    
                          <p style="text-align: center; font-size: 14px; color: #666666;">
                            Atenciosamente, Fire Training
                          </p>
                    
                        </div>
                      </body>
                    </html>
                    """.formatted(mensagemStatus, aluno, telefoneAluno, nomeCondominio, observacao, data, hora);

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
