package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.domain.DeliveryStatusHistory;
import ru.skillbox.paymentservice.domain.OrderStatus;


import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select h from DeliveryStatusHistory h where h.delivery.id = :id")
    List<DeliveryStatusHistory> findDeliveryStatusHistoryById(long id);

    Optional<Delivery> findByOrderId(long orderId);

    Optional<Delivery> findById(long id);

    int countAllByStatus(OrderStatus status);

    long count();
}
