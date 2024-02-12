package ru.skillbox.inventoryservice.service;

import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.OrderStatus;
import ru.skillbox.inventoryservice.domain.PaymentKafkaDto;
import ru.skillbox.inventoryservice.exception.InventoryException;


import java.util.List;
import java.util.Optional;

public interface InventoryService {


    Optional<Inventory> createInventory(PaymentKafkaDto paymentKafkaDto);

    void updateInventoryStatus(long id, OrderStatus status) throws InventoryException;


    void rollbackInventory(long id) throws InventoryException;
    Inventory getInventoryById(long id) throws InventoryException;

    Optional <Inventory> getInventoryByOrderId(long orderId);
    List<Inventory> findAll();

    Inventory save(Inventory inventory);

}
