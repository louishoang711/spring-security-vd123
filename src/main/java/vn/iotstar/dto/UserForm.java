package vn.iotstar.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data public class UserForm {
    @NotBlank private String username;
    @Email @NotBlank private String email;
    @NotBlank private String fullName;
    private String imageUrl;
    private String roleName = "ROLE_USER";
    private boolean enabled = true;
    private String password;
}
