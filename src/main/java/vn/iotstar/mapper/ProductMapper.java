package vn.iotstar.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.iotstar.dto.ProductDto;
import vn.iotstar.dto.ProductForm;
import vn.iotstar.entity.Product;
@Mapper(componentModel = "spring") public interface ProductMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    ProductDto toDto(Product product);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "imagePublicId", ignore = true)
    Product toEntity(ProductForm form);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "imagePublicId", ignore = true)
    void updateEntity(ProductForm form, @MappingTarget Product product);
}
