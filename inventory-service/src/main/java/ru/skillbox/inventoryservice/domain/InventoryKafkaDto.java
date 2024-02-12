package ru.skillbox.inventoryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.inventoryservice.handler.Event;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryKafkaDto implements Event {
    private Long id;

    private String status;

    private String creationTime;

    private String modifiedTime;
    @Override
    public String getEvent() {
        return "Inventory";
    }

    public static InventoryKafkaDto toKafkaDto(PaymentKafkaDto paymentKafkaDto) {
        return new InventoryKafkaDto(
                paymentKafkaDto.getId(),
                paymentKafkaDto.getStatus(),
                paymentKafkaDto.getCreationTime(),
                paymentKafkaDto.getModifiedTime());
    }
}
