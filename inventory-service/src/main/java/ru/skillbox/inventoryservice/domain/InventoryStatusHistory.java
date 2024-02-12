package ru.skillbox.inventoryservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
public class InventoryStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "comment")
    private String comment;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    public InventoryStatusHistory(Long id, OrderStatus status, String comment, Inventory inventory) {
        this.id = id;
        this.status = status;
        this.comment = comment;
        this.inventory = inventory;
        this.timestamp = LocalDateTime.now();
    }
}
