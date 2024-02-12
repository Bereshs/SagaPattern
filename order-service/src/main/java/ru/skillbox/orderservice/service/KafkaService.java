package ru.skillbox.orderservice.service;

import ru.skillbox.orderservice.domain.OrderKafkaDto;

public interface KafkaService {

    void produce(OrderKafkaDto orderKafkaDto);
}
