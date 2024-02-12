package ru.skillbox.inventoryservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.OrderStatus;
import ru.skillbox.inventoryservice.domain.PaymentKafkaDto;
import ru.skillbox.inventoryservice.exception.InventoryException;
import ru.skillbox.inventoryservice.repository.InventoryRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;


    @Override
    public Optional<Inventory> createInventory(PaymentKafkaDto paymentKafkaDto) {
        Inventory newInventory = new Inventory(
                paymentKafkaDto.getId(),
                OrderStatus.valueOf(paymentKafkaDto.getStatus()),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        newInventory.addStatusHistory(OrderStatus.valueOf(paymentKafkaDto.getStatus()),  "creation inventory request");
        Inventory inventory = save(newInventory);

        return Optional.of(inventory);
    }

    @Override
    public void updateInventoryStatus(long id, OrderStatus status) throws InventoryException {
        Inventory inventory = getInventoryById(id);
        inventory.setStatus(status);
        inventory.addStatusHistory(status, "update status");
        save(inventory);
    }


    @Override
    public void rollbackInventory(long id) throws InventoryException {
        Inventory inventory = getInventoryById(id);
        inventory.setStatus(OrderStatus.INVENTMENT_FAILED);
        inventory.addStatusHistory(OrderStatus.INVENTMENT_FAILED, "rolling back transaction");
        save(inventory);
    }

    @Override
    public Inventory getInventoryById(long id) throws InventoryException {
        var inventoryOptional = inventoryRepository.getInventoryById(id);
        if(inventoryOptional.isEmpty()) {
            throw  new InventoryException("Wrong id");
        }
        return inventoryOptional.get();
    }

    @Override
    public Optional<Inventory> getInventoryByOrderId(long orderId) {
        return inventoryRepository.getInventoryByOrderId(orderId);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory save(Inventory inventory) {
        inventory.setModificationTime(LocalDateTime.now());
        return inventoryRepository.save(inventory);

    }
}