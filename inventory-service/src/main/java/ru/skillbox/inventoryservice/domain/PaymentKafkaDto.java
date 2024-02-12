package ru.skillbox.inventoryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.inventoryservice.handler.Event;


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


}
