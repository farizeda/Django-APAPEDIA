package apap.tk.apapedia.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice = 0;

    @Column(name = "status", nullable = false)
    private Integer status = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> OrderItems;
}
