package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.paymentservice.domain.Delivery;
import ru.skillbox.paymentservice.domain.OrderStatus;
import ru.skillbox.paymentservice.repository.DeliveryRepository;
import ru.skillbox.paymentservice.service.DeliveryService;

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
@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private DeliveryService deliveryService;

    private Delivery delivery;
    private List<Delivery> deliveryList;

    @BeforeEach
    public void setUp() {

        delivery =  new Delivery(1L, OrderStatus.REGISTERED, LocalDateTime.now(), LocalDateTime.now());
        deliveryList = new ArrayList<>();
        deliveryList.add(delivery);
        deliveryList.add(new Delivery(2L, OrderStatus.DELIVERED, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    public void deliveryListTest() throws Exception {
        Mockito.when(deliveryService.findAll()).thenReturn(deliveryList);
        mockMvc.perform(get("/delivery/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(OrderStatus.DELIVERED.toString())));
    }

    @Test
    public void deliveryByOrderIdTest() throws Exception {
        Mockito.when(deliveryService.getDeliveryByOrderId(Mockito.any(long.class))).thenReturn(Optional.of(delivery));
        mockMvc.perform(get("/delivery/order/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(OrderStatus.REGISTERED.toString())));
    }

    @Test
    public void deliveryByIdTest() throws Exception {
        Mockito.when(deliveryService.getDeliveryById(Mockito.any(long.class))).thenReturn(delivery);
        mockMvc.perform(get("/delivery/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(OrderStatus.REGISTERED.toString())));
    }

}