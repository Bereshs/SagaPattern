package ru.skillbox.paymentservice.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.OrderKafkaDto;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.expception.PaymentErrorException;

import java.util.List;
import java.util.Optional;

public interface PaymentService {



    void increaseAccount(long paymentId, long amount, String comment) throws PaymentErrorException;

    void decreaseAccount(long paymentId, long amount, String comment) throws PaymentErrorException;

    Optional<Payment> createPayment(OrderKafkaDto orderKafkaDto) throws PaymentErrorException;

    void updatePaymentStatus(long paymentId, long amount, OrderStatus status, String comment) throws PaymentErrorException;

    void rollbackTransactionPayment(long paymentId, OrderStatus status) throws PaymentErrorException;

    Payment getPaymentById(long id) throws PaymentErrorException;

    Optional<Payment> getPaymentByOrderId(long orderId);

    List<Payment> findAll();
}
