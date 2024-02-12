package ru.skillbox.paymentservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.domain.DeliveryStatusHistory;
import ru.skillbox.paymentservice.domain.OrderStatus;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@DataJpaTest
class DeliveryRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    void findDeliveryStatusHistoryByIdTest() {

        Delivery delivery = new Delivery(1L, OrderStatus.REGISTERED, LocalDateTime.now(), LocalDateTime.now());
        delivery.addStatusHistory(OrderStatus.DELIVERED, "for test");
        entityManager.persist(delivery);
        entityManager.flush();

        List<DeliveryStatusHistory> deliveryStatusHistories = deliveryRepository.findDeliveryStatusHistoryById(1L);

        Delivery gotDelivery = deliveryRepository.findByOrderId(1L).orElse(null);

        assertThat(gotDelivery!=null);
        assertThat(gotDelivery.getOrderId()==1L);
        assertThat(deliveryStatusHistories.get(0).getStatus().equals(OrderStatus.INVENTED));

    }


}