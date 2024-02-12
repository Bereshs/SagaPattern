package ru.skillbox.paymentservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.paymentservice.handler.Event;

@AllArgsConstructor
@Data
public class OrderKafkaDto implements Event {

    private Long id;

    private String status;

    private String creationTime;

    private String modifiedTime;
    private long cost;

    @Override
    public String getEvent() {
        return "order";
    }
}
