package vn.iotstar.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.iotstar.dto.ProductDto;
import vn.iotstar.entity.Product;
@Mapper(componentModel = "spring") public interface ProductMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    ProductDto toDto(Product product);
}
