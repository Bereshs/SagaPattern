package ru.skillbox.paymentservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
@Entity
public class DeliveryStatusHistory {
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
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    public DeliveryStatusHistory(Long id, OrderStatus status, String comment, Delivery delivery) {
        this.id = id;
        this.status = status;
        this.comment = comment;
        this.delivery = delivery;
        this.timestamp = LocalDateTime.now();
    }
}
