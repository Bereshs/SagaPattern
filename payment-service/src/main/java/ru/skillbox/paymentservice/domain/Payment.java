package ru.skillbox.paymentservice.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modificationTime;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<PaymentStatusHistory> paymentStatusHistory = new ArrayList<>();

    public Payment(Long orderId, Long balance, OrderStatus status) {
        this.orderId = orderId;
        this.balance = balance;
        this.status = status;
        this.creationTime = LocalDateTime.now();
        this.modificationTime = LocalDateTime.now();

    }

    public void addStatusHistory(OrderStatus status, Long amount, String comment) {
        getPaymentStatusHistory().add(new PaymentStatusHistory(null, amount, status, comment, this));
    }
}
