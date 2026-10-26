package com.sptech.school.fira_manager_api.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NOTIFICACAO = "exchange.notificacao";

    public static final String ROUTING_KEY_ALUNO = "notificacao.aluno";
    public static final String ROUTING_KEY_PROFESSOR = "notificacao.professor";

    public static final String FILA_NOTIFICACAO_ALUNO = "fila.notificacao.aluno";
    public static final String FILA_NOTIFICACAO_PROFESSOR = "fila.notificacao.professor";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange exchangeNotificacao() {
        return new DirectExchange(EXCHANGE_NOTIFICACAO, true, false);
    }

    @Bean
    public Queue filaNotificacaoAluno() {
        return QueueBuilder.durable(FILA_NOTIFICACAO_ALUNO).build();
    }

    @Bean
    public Queue filaNotificacaoProfessor() {
        return QueueBuilder.durable(FILA_NOTIFICACAO_PROFESSOR).build();
    }

    @Bean
    public Binding bindingAluno(Queue filaNotificacaoAluno, DirectExchange exchangeNotificacao) {
        return BindingBuilder.bind(filaNotificacaoAluno).to(exchangeNotificacao).with(ROUTING_KEY_ALUNO);
    }

    @Bean
    public Binding bindingProfessor(Queue filaNotificacaoProfessor, DirectExchange exchangeNotificacao) {
        return BindingBuilder.bind(filaNotificacaoProfessor).to(exchangeNotificacao).with(ROUTING_KEY_PROFESSOR);
    }
}