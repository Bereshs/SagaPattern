package ru.skillbox.inventoryservice.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.inventoryservice.domain.Inventory;
import ru.skillbox.inventoryservice.domain.InventoryStatusHistory;
import ru.skillbox.inventoryservice.domain.OrderStatus;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void findInventoryStatusHistoryByIdTest() {
        Inventory inventory = new Inventory(1L, OrderStatus.REGISTERED,LocalDateTime.now(), LocalDateTime.now());
        inventory.addStatusHistory(OrderStatus.INVENTED, "for test");
        entityManager.persist(inventory);
        entityManager.flush();

        List<InventoryStatusHistory> inventoryStatusHistories = inventoryRepository.findInventoryStatusHistoryById(1L);
        Inventory gotInventory = inventoryRepository.getInventoryById(1L).orElse(null);

        assertThat(gotInventory!=null);
        assertThat(gotInventory.getOrderId()==1L);
        assertThat(inventoryStatusHistories.get(0).getStatus().equals(OrderStatus.INVENTED));

    }

}