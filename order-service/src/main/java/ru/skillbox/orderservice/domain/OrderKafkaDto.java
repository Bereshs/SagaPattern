package ru.skillbox.orderservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderKafkaDto {

    private Long id;

    private String status;

    private String creationTime;

    private String modifiedTime;


    private Long cost;

    public static OrderKafkaDto toKafkaDto(Order order) {

        return new OrderKafkaDto(
                order.getId(),
                order.getStatus().toString(),
                order.getCreationTime().toString(),
                order.getModifiedTime().toString(),
                order.getCost()
        );

    }
}
