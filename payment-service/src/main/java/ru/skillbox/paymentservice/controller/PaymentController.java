package ru.skillbox.paymentservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.domain.PaymentDto;
import ru.skillbox.paymentservice.expception.PaymentErrorException;
import ru.skillbox.paymentservice.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor

public class PaymentController {


    private final PaymentService paymentService;

    @Operation(summary = "List all payments in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/")
    public List<Payment> listPayments() {
        return paymentService.findAll();
    }


    @Operation(summary = "Fill account", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/")
    public ResponseEntity<?> fillAccount(@RequestBody PaymentDto input) throws PaymentErrorException {
        Payment payment = paymentService.getPaymentByOrderId(input.getOrderId()).orElseThrow(() -> new PaymentErrorException("Order orderId: %d not found".formatted(input.getOrderId())));
        paymentService.increaseAccount(payment.getId(), input.getAmount(), "Fill account success");
        return ResponseEntity.ok("fill account completed");
    }

    @Operation(summary = "Get an payment in system by order id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{orderId}")
    public Payment listPayment(@PathVariable @Parameter(description = "Id of order") Long orderId) throws PaymentErrorException {
        return paymentService.getPaymentByOrderId(orderId).orElseThrow(() -> new PaymentErrorException("Not found orderId: " + orderId));
    }


}
