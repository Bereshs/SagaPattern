package ru.skillbox.paymentservice.handler;

import ru.skillbox.paymentservice.expception.PaymentErrorException;

public interface EventHandler <T extends Event, R extends Event>{
    R handleEvent(T event);
}
