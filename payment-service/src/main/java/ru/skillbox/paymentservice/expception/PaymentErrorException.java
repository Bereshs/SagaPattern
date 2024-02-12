package ru.skillbox.paymentservice.expception;

public class PaymentErrorException extends Exception {
    public PaymentErrorException(String message) {
        super(message);
    }
}
