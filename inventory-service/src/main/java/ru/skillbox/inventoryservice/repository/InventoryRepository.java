package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.InventoryStatusHistory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("select h from InventoryStatusHistory h where h.inventory.id = :id")
    List<InventoryStatusHistory> findInventoryStatusHistoryById(long id);

    Optional<Inventory> getInventoryByOrderId(long orderId);

    Optional<Inventory> getInventoryById(long id);
}
