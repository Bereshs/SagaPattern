package ru.skillbox.orderservice.service;

import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.StatusDto;

import java.util.Optional;

public interface OrderService {

    Optional<Order> addOrder(OrderDto orderDto);

    void updateOrderStatus(Long id, StatusDto statusDto);
}
