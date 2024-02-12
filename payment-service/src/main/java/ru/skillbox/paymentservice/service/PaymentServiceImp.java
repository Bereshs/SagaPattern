package ru.skillbox.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.OrderKafkaDto;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.domain.PaymentStatusHistory;
import ru.skillbox.paymentservice.expception.PaymentErrorException;
import ru.skillbox.paymentservice.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service

public class PaymentServiceImp implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImp(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @Override
    public void increaseAccount(long paymentId, long amount, String comment) throws PaymentErrorException {
        var payment = getPaymentById(paymentId);
        long balanceAfterIncrease = payment.getBalance() + amount;
        payment.setBalance(balanceAfterIncrease);
        save(payment);
        updatePaymentStatus(paymentId, amount, OrderStatus.FILL, comment);
    }


    @Transactional
    @Override
    public void decreaseAccount(long paymentId, long amount, String comment) throws PaymentErrorException {
        var payment = getPaymentById(paymentId);
        long amountAfterDecrease = payment.getBalance() - amount;
        if (amountAfterDecrease < 0) {
            throw new PaymentErrorException("Not money on Account id:" + payment.getId());
        }
        payment.setBalance(amountAfterDecrease);
        save(payment);
        updatePaymentStatus(payment.getId(), (-1) * amount, OrderStatus.PAID, comment);
    }


    @Transactional
    @Override
    public Optional<Payment> createPayment(OrderKafkaDto orderKafkaDto) throws PaymentErrorException {
        Payment newPayment = new Payment(
                orderKafkaDto.getId(),
                0L,
                OrderStatus.valueOf(orderKafkaDto.getStatus())
        );
        Payment payment = save(newPayment);
        updatePaymentStatus(payment.getId(), 0, OrderStatus.valueOf(orderKafkaDto.getStatus()), "creation payment request");
        return Optional.of(newPayment);
    }

    @Transactional
    @Override
    public void updatePaymentStatus(long paymentId, long amount, OrderStatus status, String comment) throws PaymentErrorException {
        var payment = getPaymentById(paymentId);
        payment.setStatus(status);
        payment.addStatusHistory(status, amount, comment);
        save(payment);
    }


    @Override
    public void rollbackTransactionPayment(long paymentId, OrderStatus status) throws PaymentErrorException {
        var payment = getPaymentById(paymentId);
        List<PaymentStatusHistory> paymentStatusHistoryList = paymentRepository.findPaymentStatusHistoryById(payment.getId());
        rollbackAllPaidTransaction(paymentStatusHistoryList, paymentId);
        rollbackAllFillTransaction(paymentStatusHistoryList, paymentId);
    }

    private void rollbackAllFillTransaction(List<PaymentStatusHistory> paymentStatusHistoryList, long paymentId) throws PaymentErrorException {
        for (PaymentStatusHistory item : paymentStatusHistoryList.stream().filter((item) -> item.getAmount() > 0).toList()) {
            Logger.getLogger("int").info("just info fill" + item.getAmount() + " " + item.getStatus());
            decreaseAccount(paymentId, Math.abs(item.getAmount()), "Rollback transaction");
        }
    }

    private void rollbackAllPaidTransaction(List<PaymentStatusHistory> paymentStatusHistoryList, long paymentId) throws PaymentErrorException {
        for (PaymentStatusHistory item : paymentStatusHistoryList.stream().filter((item) -> item.getAmount() < 0).toList()) {
            Logger.getLogger("int").info("just info paid" + item.getAmount() + " " + item.getStatus());
            increaseAccount(paymentId, Math.abs(item.getAmount()), "Rollback transaction");
        }
    }


    @Override
    public Payment getPaymentById(long id) throws PaymentErrorException {
        Optional<Payment> paymentOptional = paymentRepository.getPaymentById(id);
        if (paymentOptional.isEmpty()) {
            throw new PaymentErrorException("Entity with  id: " + id + " not found");
        }
        return paymentOptional.get();

    }

    @Override
    public Optional<Payment> getPaymentByOrderId(long orderId) {
        return paymentRepository.getPaymentByOrderId(orderId);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }


    public Payment save(Payment payment) {
        payment.setModificationTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }


}
