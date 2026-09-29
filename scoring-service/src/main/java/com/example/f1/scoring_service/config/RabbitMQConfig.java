package com.example.f1.scoring_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RACE_FINISHED_EXCHANGE = "race.finished.exchange";
    public static final String RACE_FINISHED_QUEUE = "race.finished.queue";
    public static final String RACE_FINISHED_ROUTING_KEY = "race.finished";

    @Bean
    public DirectExchange raceFinishedExchange() {
        return new DirectExchange(RACE_FINISHED_EXCHANGE);
    }

    @Bean
    public Queue raceFinishedQueue() {
        return new Queue(RACE_FINISHED_QUEUE, true);
    }

    @Bean
    public Binding raceFinishedBinding(Queue raceFinishedQueue, DirectExchange raceFinishedExchange) {
        return BindingBuilder.bind(raceFinishedQueue).to(raceFinishedExchange).with(RACE_FINISHED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
