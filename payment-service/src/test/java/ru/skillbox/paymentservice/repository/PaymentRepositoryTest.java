package ru.skillbox.paymentservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.domain.PaymentStatusHistory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ActiveProfiles("test")
@DataJpaTest
class PaymentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepositoryJpa;

    @Test
    void findPaymentStatusHistoryByIdTest() {
        Payment payment = new Payment(1L, 100L, OrderStatus.REGISTERED);
        payment.setOrderId(1L);
        payment.addStatusHistory(OrderStatus.PAID,100L, "Test paid");
        entityManager.persist(payment);
        entityManager.flush();

        List<PaymentStatusHistory> gotPaymentStatusHistory = paymentRepositoryJpa.findPaymentStatusHistoryById(1L);
        Payment gotPayment = paymentRepositoryJpa.getPaymentByOrderId(1L).orElse(null);
        assertThat(gotPayment!=null);
        assertThat(gotPayment.getBalance()==100L);
        assertThat(gotPaymentStatusHistory.get(0).getStatus().equals(OrderStatus.PAID));

    }
}