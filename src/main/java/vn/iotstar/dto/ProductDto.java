package vn.iotstar.dto;
import java.math.BigDecimal;
import lombok.Data;
@Data public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long userId;
    private String userName;
}
