package ru.skillbox.paymentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.paymentservice.handler.Event;

@AllArgsConstructor
@Data
public class PaymentKafkaDto implements Event {
    private Long id;

    private String status;

    private String creationTime;

    private String modifiedTime;


    @Override
    public String getEvent() {
        return "payment";
    }

    public static PaymentKafkaDto toKafkaDto(OrderKafkaDto orderKafkaDto) {
        return new PaymentKafkaDto(orderKafkaDto.getId(), orderKafkaDto.getStatus(), orderKafkaDto.getCreationTime(), orderKafkaDto.getModifiedTime());

    }
}
