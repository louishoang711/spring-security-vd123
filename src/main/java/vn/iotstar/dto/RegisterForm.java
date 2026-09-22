package vn.iotstar.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data public class RegisterForm {
    @NotBlank @Size(max = 50) private String username;
    @Email @NotBlank private String email;
    @NotBlank private String fullName;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank private String confirmPassword;
}
