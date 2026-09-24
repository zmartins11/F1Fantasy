package com.example.f1.scoring_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RACE_FINISHED_QUEUE = "race.finished";
    public static final String F1_EXCHANGE = "f1.exchange";
    public static final String RACE_FINISHED_ROUTING_KEY = "race.finished";

    @Bean
    public Queue raceFinishedQueue() {
        return new Queue(RACE_FINISHED_QUEUE, true);
    }

    @Bean
    public TopicExchange f1Exchange() {
        return new TopicExchange(F1_EXCHANGE);
    }

    @Bean
    public Binding raceFinishedBinding(
            Queue raceFinishedQueue,
            TopicExchange f1Exchange) {

        return BindingBuilder
                .bind(raceFinishedQueue)
                .to(f1Exchange)
                .with(RACE_FINISHED_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
