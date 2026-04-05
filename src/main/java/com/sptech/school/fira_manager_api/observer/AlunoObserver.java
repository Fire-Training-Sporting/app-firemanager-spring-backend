package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.service.EmailService;

import java.time.format.DateTimeFormatter;

public class AlunoObserver implements Observer {

    private final Long id;
    private final EmailService emailService;
    private static final DateTimeFormatter DATA_FORMATADA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA_FORMATADA = DateTimeFormatter.ofPattern("HH:mm");

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

            String data = agendamento.getData().format(DATA_FORMATADA);
            String hora = agendamento.getHoraInicio().format(HORA_FORMATADA);

            String assunto = "";
            String mensagemStatus = "";

            switch (agendamento.getStatus().toLowerCase()) {
                case "pendente":
                    assunto = "[FireManager] - Nova Aula Agendada!";
                    mensagemStatus = String.format("Você tem uma nova aula agendada com %s no dia %s às %s. [ID Agendamento: %d]",
                            agendamento.getProfessor().getNome(), data, hora, agendamento.getId());
                    break;
                case "cancelado":
                    assunto = "[FireManager] - Agendamento Cancelado";
                    mensagemStatus = String.format("Seu agendamento foi cancelado. [ID Agendamento: %d]",
                            agendamento.getId());
                    break;
                case "confirmado":
                    assunto = "[FireManager] - Aula Confirmada";
                    mensagemStatus = String.format("Sua aula com %s é daqui menos de 24h! [ID Agendamento: %d]",
                            agendamento.getProfessor().getNome(), agendamento.getId());
                    break;
                case "finalizado":
                    assunto = "[FireManager] - Aula Concluída";
                    mensagemStatus = String.format("Sua aula com %s foi concluída. Obrigado pela confiança em nosso serviço! [ID Agendamento: %d]",
                            agendamento.getProfessor().getNome(), agendamento.getId());
                    break;
            }

            String mensagem = """
                    <html>
                      <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;">
                        <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 10px; border-top: 6px solid #ff6600; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    
                          <h2 style="color: #ff6600; text-align: center;">Olá!</h2>
                    
                          <p style="font-size: 16px; color: #333333; text-align: center;">
                            %s
                          </p>
                    
                          <p style="text-align: center; font-size: 14px; color: #666666;">
                            Atenciosamente, Fire Training
                          </p>
                    
                        </div>
                      </body>
                    </html>
            """.formatted(mensagemStatus);

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


