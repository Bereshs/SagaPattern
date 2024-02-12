package ru.skillbox.paymentservice.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.paymentservice.consumer.EventConsumer;
import ru.skillbox.paymentservice.domain.InventoryKafkaDto;

import java.util.function.Consumer;

@Configuration
@AllArgsConstructor
public class DeliveryServiceConfig {
    private final EventConsumer<InventoryKafkaDto> deliveryEventConsumer;

    @Bean
    Consumer<InventoryKafkaDto> deliveryEventConsumer() {
        return deliveryEventConsumer::consumeEvent;
    }
}
