package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.service.PaymentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    private Payment payment;

    private List<Payment> paymentList;

    @BeforeEach
    public void setUp() {
        payment = new Payment(1L, 100L,
                OrderStatus.PAID);

        paymentList = new ArrayList<>();
        paymentList.add(payment);
        paymentList.add(new Payment(1L, 200L,
                OrderStatus.PAYMENT_FAILED));
    }

    @Test
    public void listPaymentsTest() throws Exception {
        Mockito.when(paymentService.findAll()).thenReturn(paymentList);
        mockMvc.perform(get("/payment/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString(payment.getStatus().toString())));
    }

    @Test
    public void listPaymentTest() throws Exception {
        Mockito.when(paymentService.getPaymentByOrderId((Mockito.any(long.class)))).thenReturn(Optional.of(payment));
        mockMvc.perform(get("/payment/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString(payment.getStatus().toString())));
    }


    @Test
    public void listPaymentWrongParameterTest() throws Exception {
        Mockito.when(paymentService.getPaymentByOrderId((Mockito.any(long.class)))).thenReturn(Optional.empty());
        mockMvc.perform(get("/payment/1"))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(content().string(containsString("Not found orderId: 1")));
    }

}