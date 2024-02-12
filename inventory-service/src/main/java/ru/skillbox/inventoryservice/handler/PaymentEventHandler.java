package ru.skillbox.inventoryservice.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.InventoryKafkaDto;
import ru.skillbox.inventoryservice.domain.OrderStatus;
import ru.skillbox.inventoryservice.domain.PaymentKafkaDto;
import ru.skillbox.inventoryservice.exception.InventoryException;
import ru.skillbox.inventoryservice.service.InventoryService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentEventHandler implements EventHandler<PaymentKafkaDto, InventoryKafkaDto> {

    private final InventoryService inventoryService;

    @Override
    public InventoryKafkaDto handleEvent(PaymentKafkaDto event) {
        InventoryKafkaDto kafkaDto = InventoryKafkaDto.toKafkaDto(event);
        var inventoryDb = inventoryService.getInventoryByOrderId(event.getId());
        log.info("staring order event handler is new order: " + inventoryDb.isEmpty() + " is failed status:" + isFailStatus(event.getStatus()));

        if (inventoryDb.isEmpty()) return createInventoryKafkaDtoOrFailed(event);

        if (isFailStatus(event.getStatus())) {
            rollingBackInventoryTransactions(inventoryDb.get().getId());
            return kafkaDto;
        }

        if (inventoryDb.get().getStatus().toString().equals(event.getStatus())) return kafkaDto;


        updateInventoryStatus(inventoryDb.get().getId(), OrderStatus.valueOf(event.getStatus()));
        return kafkaDto;
    }

    private InventoryKafkaDto createInventoryKafkaDtoOrFailed(PaymentKafkaDto event) {
        InventoryKafkaDto kafkaDto = InventoryKafkaDto.toKafkaDto(event);
        try {
            Inventory newInventory = inventoryService.createInventory(event).orElseThrow(() -> new InventoryException("error creating inventory request orderId %d".formatted(event.getId())));
            inventEmulator(newInventory);
            return kafkaDto;
        } catch (InventoryException e) {
            log.error("error during execution " + e.getMessage());
            kafkaDto.setStatus(OrderStatus.INVENTMENT_FAILED.toString());
            return kafkaDto;
        }

    }

    private void updateInventoryStatus(long id, OrderStatus status) {
        try {
            inventoryService.updateInventoryStatus(id, status);
        } catch (InventoryException e) {
            log.error(e.getMessage());
        }

    }

    private void inventEmulator(Inventory inventory) {
        inventory.setStatus(OrderStatus.INVENTED);
        inventory.addStatusHistory(OrderStatus.INVENTED, "invent emulator");
        inventoryService.save(inventory);
    }

    private void rollingBackInventoryTransactions(long id) {
        try {
            inventoryService.rollbackInventory(id);
        } catch (InventoryException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isFailStatus(String status) {
        return status.toLowerCase().contains("fail");
    }
}


