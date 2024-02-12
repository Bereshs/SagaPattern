package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.domain.PaymentStatusHistory;

import java.util.List;
import java.util.Optional;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select h from PaymentStatusHistory h where h.payment.id = :id")
    List<PaymentStatusHistory> findPaymentStatusHistoryById(long id);

    Optional<Payment> getPaymentByOrderId(long orderId);


    Optional<Payment> getPaymentById(long id);

}
