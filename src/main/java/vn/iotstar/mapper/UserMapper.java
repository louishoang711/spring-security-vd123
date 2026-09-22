package vn.iotstar.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.iotstar.dto.UserDto;
import vn.iotstar.dto.UserForm;
import vn.iotstar.entity.User;
@Mapper(componentModel = "spring") public interface UserMapper {
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "productCount", ignore = true)
    UserDto toDto(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserForm form);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(UserForm form, @MappingTarget User user);
}
