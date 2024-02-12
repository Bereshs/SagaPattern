package ru.skillbox.paymentservice.domain;

import lombok.Data;

@Data

public class OrderStatusDto {
    private String status;

    private ServiceName serviceName;

    private String comment;

    public OrderStatusDto(String status, String comment) {
        this.status = status;
        this.serviceName = ServiceName.DELIVERY_SERVICE;
        this.comment = comment;
    }


    public static OrderStatusDto toOrderStatusDto(InventoryKafkaDto inventoryKafkaDto) {
        return new OrderStatusDto(inventoryKafkaDto.getStatus(), "from delivery service");

    }

}
