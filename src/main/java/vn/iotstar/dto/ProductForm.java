package vn.iotstar.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
@Data public class ProductForm {
    @NotBlank private String name;
    private String description;
    @NotNull @DecimalMin("0.00") private BigDecimal price;
}
