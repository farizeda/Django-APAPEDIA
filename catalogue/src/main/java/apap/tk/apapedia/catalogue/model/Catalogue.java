package apap.tk.apapedia.catalogue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogue")
@SQLDelete(sql = "UPDATE catalogue SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@JsonIgnoreProperties(value={"category"}, allowSetters = true)
public class Catalogue {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "price", nullable = false)
    private Integer price = 0;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "image", nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name="is_deleted")
    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
