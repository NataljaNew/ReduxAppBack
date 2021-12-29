package resto.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "VARCHAR(36)", updatable = false)
    @Type(type = "uuid-char")
    private UUID id;
    @NotBlank
    @Size(min=5, max=50)
//    message = "{validation.size.product.name}"
    private String title;
    @NotBlank
    private String img;
    @Size(max=300)
    @NotBlank
    private String category;
    @PositiveOrZero
    @Max(1000)
    private int quantity;
    @Positive
    private BigDecimal price;
}
