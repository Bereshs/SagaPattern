package ru.skillbox.paymentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDto {
    private long orderId;
    private long amount;
}
