package ru.skillbox.paymentservice.service;

import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.domain.InventoryKafkaDto;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.exception.DeliveryException;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {

    Optional<Delivery> createDelivery(InventoryKafkaDto inventoryKafkaDto);

    void updateDeliveryStatus(long id, OrderStatus status) throws DeliveryException;


    void rollbackDelivery(long id) throws DeliveryException;

    Delivery getDeliveryById(long id) throws DeliveryException;

    Optional<Delivery> getDeliveryByOrderId(long orderId);

    List<Delivery> findAll();

    Delivery save(Delivery delivery);

    int countAllByStatus(OrderStatus status);

    long countAll();

}
