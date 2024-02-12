package ru.skillbox.inventoryservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.OrderStatus;
import ru.skillbox.inventoryservice.exception.InventoryException;
import ru.skillbox.inventoryservice.repository.InventoryRepository;
import ru.skillbox.inventoryservice.service.InventoryService;

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
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;
    @MockBean
    private InventoryRepository inventoryRepository;
    private Inventory inventory;
    private List<Inventory> inventoryList;

    @BeforeEach
    public void setUp() {

        inventory = new Inventory(1L, OrderStatus.INVENTED, LocalDateTime.now(), LocalDateTime.now());

        inventoryList = new ArrayList<>();
        inventoryList.add(inventory);
        inventoryList.add(new Inventory(2L,
                OrderStatus.INVENTMENT_FAILED,
                LocalDateTime.now(),
                LocalDateTime.now()));
    }

    @Test
    public void inventoryListTest() throws Exception {
        Mockito.when(inventoryService.findAll()).thenReturn(inventoryList);
        mockMvc.perform(get("/inventory/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(inventory.getStatus().toString())));
    }

    @Test
    public void inventoryByOrderIdTest() throws Exception {
        Mockito.when(inventoryService.getInventoryByOrderId(Mockito.any(long.class))).thenReturn(Optional.of(inventory));
        mockMvc.perform(get("/inventory/order/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(inventory.getStatus().toString())));
    }

    @Test
    public void inventoryByIdTest() throws Exception {
        Mockito.when(inventoryService.getInventoryById(Mockito.any(long.class))).thenReturn(inventory);
        mockMvc.perform(get("/inventory/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(inventory.getStatus().toString())));

    }
}