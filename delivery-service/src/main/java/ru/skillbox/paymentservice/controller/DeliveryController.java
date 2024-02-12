package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.exception.DeliveryException;
import ru.skillbox.paymentservice.service.DeliveryService;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@AllArgsConstructor
public class DeliveryController {

    private DeliveryService deliveryService;

    @Operation(summary = "List all delivery requests in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/")
    public List<Delivery> deliveryList() {
        return deliveryService.findAll();
    }

    @Operation(summary = "Get delivery by orderId in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/{orderId}")
    public Delivery deliveryByOrderId(@PathVariable @Parameter(description = "Id of order") Long orderId) throws DeliveryException {
        return deliveryService.getDeliveryByOrderId(orderId).orElseThrow(() -> new DeliveryException("Wrong order Id"));
    }

    @Operation(summary = "Get delivery by id in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public Delivery deliveryById(@PathVariable @Parameter(description = "inventory id") Long id) throws DeliveryException {
        return deliveryService.getDeliveryById(id);
    }


}
