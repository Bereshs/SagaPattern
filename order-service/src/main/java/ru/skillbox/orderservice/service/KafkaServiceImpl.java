package ru.skillbox.orderservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.orderservice.domain.OrderKafkaDto;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    private final KafkaTemplate<Long, OrderKafkaDto> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<Long, OrderKafkaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(OrderKafkaDto orderKafkaDto) {
        kafkaTemplate.send(kafkaTopic, orderKafkaDto);
        logger.info("Sent message to Kafka -> '{}'", orderKafkaDto);
    }
}
