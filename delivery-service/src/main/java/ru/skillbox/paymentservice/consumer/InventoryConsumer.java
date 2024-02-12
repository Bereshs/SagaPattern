package ru.skillbox.paymentservice.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.domain.InventoryKafkaDto;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.domain.OrderStatusDto;
import ru.skillbox.paymentservice.exception.DeliveryException;
import ru.skillbox.paymentservice.http.HttpClient;
import ru.skillbox.paymentservice.service.DeliveryService;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryConsumer implements EventConsumer<InventoryKafkaDto> {
    private DeliveryService deliveryService;
    private HttpClient httpClient;

    @Override
    public void consumeEvent(InventoryKafkaDto inventoryKafkaDto) {
        log.info("start event consumer id:" + inventoryKafkaDto);
        Optional<Delivery> deliveryDb = deliveryService.getDeliveryByOrderId(inventoryKafkaDto.getId());
        OrderStatusDto orderStatusDto = OrderStatusDto.toOrderStatusDto(inventoryKafkaDto);
        if (deliveryDb.isEmpty()) {
            doDelivery(inventoryKafkaDto, orderStatusDto);
            return;
        }
        if (isFailStatus(inventoryKafkaDto.getStatus())) {
            rollingBackDelivery(deliveryDb.get().getId());
        }

    }

    private void doDelivery(InventoryKafkaDto inventoryKafkaDto, OrderStatusDto orderStatusDto) {
        Optional<Delivery> delivery = deliveryService.createDelivery(inventoryKafkaDto);
        if (delivery.isPresent()) {
            Delivery deliveryObj = delivery.get();
            deliveryObj.addStatusHistory(deliveryEmulator(), "created by delivery emulator");
            deliveryService.save(deliveryObj);
            orderStatusDto.setStatus(deliveryObj.getStatus().toString());
            httpClient.patch(orderStatusDto, String.class, inventoryKafkaDto.getId().toString());
            log.info("Delivery consumer completed " + inventoryKafkaDto.getId());
        }

    }

    private void rollingBackDelivery(long id) {
        try {
            deliveryService.rollbackDelivery(id);
        } catch (DeliveryException e) {
            log.error(e.getMessage());
        }

    }

    private boolean isFailStatus(String status) {
        return status.toLowerCase().contains("fail");
    }

    private OrderStatus deliveryEmulator() {
        return OrderStatus.DELIVERY_FAILED;
  /*      double failPercent = 0.85;
        long countFailed = deliveryService.countAllByStatus(OrderStatus.DELIVERY_FAILED);
        long count = deliveryService.countAllByStatus(OrderStatus.DELIVERED);
        double currentPercent = 1 - countFailed / (double) count;
        log.info("Delivery emulator info count: "+count+" countFiled:"+countFailed+ " current:"+currentPercent);
        return OrderStatus.DELIVERY_FAILED;
    */
    }

}
