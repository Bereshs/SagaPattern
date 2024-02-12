package ru.skillbox.inventoryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.inventoryservice.domain.InventoryKafkaDto;
import ru.skillbox.inventoryservice.domain.PaymentKafkaDto;
import ru.skillbox.inventoryservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class InventoryServiceConfig {

    private final EventHandler<PaymentKafkaDto, InventoryKafkaDto> inventoryEventHandler;

    @Autowired
    public InventoryServiceConfig(EventHandler<PaymentKafkaDto, InventoryKafkaDto> paymentEventHandler) {
        this.inventoryEventHandler = paymentEventHandler;
    }


    @Bean
    public Function<PaymentKafkaDto, InventoryKafkaDto> inventoryEventProcessor() {
        return inventoryEventHandler::handleEvent;
    }

}
