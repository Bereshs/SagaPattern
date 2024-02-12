package ru.skillbox.paymentservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.paymentservice.domain.OrderKafkaDto;
import ru.skillbox.paymentservice.domain.PaymentKafkaDto;
import ru.skillbox.paymentservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class PaymentServiceConfig {

    private final EventHandler<OrderKafkaDto, PaymentKafkaDto> orderEventHandler;

    @Autowired
    public PaymentServiceConfig(EventHandler<OrderKafkaDto, PaymentKafkaDto> eventHandler) {
        this.orderEventHandler = eventHandler;
    }


    @Bean
    public Function<OrderKafkaDto, PaymentKafkaDto> orderEventProcessor() {
        return orderEventHandler::handleEvent;
    }
}
