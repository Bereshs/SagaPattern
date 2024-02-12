package ru.skillbox.paymentservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.domain.InventoryKafkaDto;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.exception.DeliveryException;
import ru.skillbox.paymentservice.repository.DeliveryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Optional<Delivery> createDelivery(InventoryKafkaDto inventoryKafkaDto) {
        Delivery newDelivery = new Delivery(
                inventoryKafkaDto.getId(),
                OrderStatus.REGISTERED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        newDelivery.addStatusHistory(OrderStatus.REGISTERED, "creation delivery request");
        Delivery delivery = save(newDelivery);

        return Optional.of(delivery);
    }

    @Override
    public void updateDeliveryStatus(long id, OrderStatus status) throws DeliveryException {
        var delivery = getDeliveryById(id);
        delivery.setStatus(status);
        delivery.addStatusHistory(status, "request change status");
        save(delivery);
    }

    @Override
    public void rollbackDelivery(long id) throws DeliveryException {
        var delivery = getDeliveryById(id);
        delivery.setStatus(OrderStatus.DELIVERY_FAILED);
        delivery.addStatusHistory(OrderStatus.DELIVERY_FAILED, "rolling back transaction");
        save(delivery);
    }

    @Override
    public Delivery getDeliveryById(long id) throws DeliveryException {
        return deliveryRepository.findByOrderId(id).orElseThrow(() -> new DeliveryException("Wrong order id"));
    }

    @Override
    public Optional<Delivery> getDeliveryByOrderId(long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    @Override
    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    @Override
    public Delivery save(Delivery delivery) {

        delivery.setModificationTime(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }

    @Override
    public int countAllByStatus(OrderStatus status) {
        return deliveryRepository.countAllByStatus(status);
    }

    @Override
    public long countAll() {
        return deliveryRepository.count();
    }

}
