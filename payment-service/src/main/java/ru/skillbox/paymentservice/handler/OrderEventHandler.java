package ru.skillbox.paymentservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.domain.*;
import ru.skillbox.paymentservice.expception.PaymentErrorException;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.service.PaymentService;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j

public class OrderEventHandler implements EventHandler<OrderKafkaDto, PaymentKafkaDto> {
    private final PaymentService paymentService;

    @Autowired
    public OrderEventHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Override
    public PaymentKafkaDto handleEvent(OrderKafkaDto event) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(event.getId());
        PaymentKafkaDto paymentKafkaDto = PaymentKafkaDto.toKafkaDto(event);
        log.info("staring payment event handler is new payment: " + payment.isEmpty() + " is failed status: " + isFailStatus(event.getStatus()));
        if (payment.isEmpty()) return createKafkaDtoOrFailed(event);

        if (isFailStatus(event.getStatus())) {
            doRollback(payment.get().getId(), OrderStatus.valueOf(event.getStatus()));
        }

        if (payment.get().getStatus().toString().equals(event.getStatus())) return paymentKafkaDto;

        updatePaymentStatus(payment.get().getId(), OrderStatus.valueOf(event.getStatus()));

        return paymentKafkaDto;
    }

    private PaymentKafkaDto createKafkaDtoOrFailed(OrderKafkaDto event) {
        PaymentKafkaDto paymentKafkaDto = PaymentKafkaDto.toKafkaDto(event);
        try {
            Payment newPayment = paymentService.createPayment(event).orElseThrow(() -> new PaymentErrorException("Error creating payment orderId:%d".formatted(event.getId())));
            doPayment(newPayment.getId(), event.getCost());
            return paymentKafkaDto;

        } catch (PaymentErrorException e) {
            paymentKafkaDto.setStatus(OrderStatus.PAYMENT_FAILED.toString());
            log.error("Error creating payment orderId:%d".formatted(event.getId()));
            return paymentKafkaDto;
        }

    }

    private void updatePaymentStatus(long paymentId, OrderStatus status) {
        try {
            paymentService.updatePaymentStatus(paymentId, 0L, status, "request for update status");
        } catch (PaymentErrorException e) {
            log.error(e.getMessage());
        }

    }

    private boolean isFailStatus(String status) {
        return status.toLowerCase().contains("fail");
    }

    public void doPayment(long paymentId, long cost) throws PaymentErrorException {
        log.info("Starting payment orderId:" + paymentId);
        doFill(paymentId, cost);
        try {
            paymentService.decreaseAccount(paymentId, cost, "paid order success");
            log.info("decrease done");
        } catch (PaymentErrorException e) {
            paymentService.updatePaymentStatus(paymentId, 0L, OrderStatus.PAYMENT_FAILED, "No amount account money");
            throw new RuntimeException(e);
        }
    }

    public void doFill(Long paymentId, long cost) throws PaymentErrorException {
        paymentService.increaseAccount(paymentId, cost, "fill account success");
    }

    public void doRollback(long paymentId, OrderStatus status) {
        log.info("rolling back payment paymentId:%d started".formatted(paymentId));
        try {
            paymentService.rollbackTransactionPayment(paymentId, status);
        } catch (PaymentErrorException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
