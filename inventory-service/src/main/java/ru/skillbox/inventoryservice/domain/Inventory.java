package ru.skillbox.inventoryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "modification_time")
    private LocalDateTime modificationTime;

    @OneToMany(
            mappedBy = "inventory",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InventoryStatusHistory> statusHistory = new ArrayList<>();

    public Inventory(long orderId, OrderStatus status, LocalDateTime creationTime, LocalDateTime modificationTime) {
        this.status = status;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.orderId=orderId;
    }

    public void addStatusHistory(OrderStatus status, String comment) {
        getStatusHistory().add(new InventoryStatusHistory(null, status, comment, this));
    }

}
