package ru.skillbox.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.exception.InventoryException;
import ru.skillbox.inventoryservice.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "List all invent requests in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/")
    public List<Inventory> inventoryList() {
        return inventoryService.findAll();
    }

    @Operation(summary = "Get inventory by orderId in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/{orderId}")
    public Inventory inventoryByOrderId(@PathVariable @Parameter(description = "Id of order") Long orderId) throws InventoryException {
        return inventoryService.getInventoryByOrderId(orderId).orElseThrow(() -> new InventoryException("Wrong order Id"));
    }

    @Operation(summary = "Get inventory by id in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public Inventory inventoryById(@PathVariable @Parameter(description = "inventory id") Long id) throws InventoryException {
        return inventoryService.getInventoryById(id);
    }




}
