package ru.skillbox.paymentservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryKafkaDto implements Event {
    private Long id;

    private String status;

    private String creationTime;

    private String modifiedTime;
    @Override
    public String getEvent() {
        return "Inventory";
    }


}
