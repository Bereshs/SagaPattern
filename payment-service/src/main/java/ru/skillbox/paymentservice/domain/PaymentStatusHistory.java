package ru.skillbox.paymentservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "payments")
public class PaymentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "comment")
    private String comment;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public PaymentStatusHistory(Long id,  Long amount, OrderStatus status, String comment, Payment payment) {
        this.id=id;
        this.status = status;
        this.comment = comment;
        this.payment = payment;
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
    }
}
