package apap.tk.apapedia.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_item")
@JsonIgnoreProperties(value = {"cart"},  allowSetters = true)
public class CartItem {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "product_price", nullable = false)
    private Integer price;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
}
