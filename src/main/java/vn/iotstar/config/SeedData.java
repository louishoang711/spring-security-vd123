package vn.iotstar.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration public class SeedData {
    @Bean CommandLineRunner seed(RoleRepository roles, UserRepository users, PasswordEncoder encoder,
            @Value("${app.admin.email}") String adminEmail, @Value("${app.admin.password}") String adminPassword,
            @Value("${app.demo.user.email}") String userEmail, @Value("${app.demo.user.password}") String userPassword) {
        return args -> {
            Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            Role user = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("ROLE_USER")));
            create(users, encoder, "admin", adminEmail, adminPassword, "Administrator", admin);
            create(users, encoder, "user01", userEmail, userPassword, "Demo User", user);
        };
    }
    private void create(UserRepository users, PasswordEncoder encoder, String username, String email,
                        String password, String fullName, Role role) {
        if (users.existsByEmailIgnoreCase(email) || users.existsByUsernameIgnoreCase(username)) return;
        User user = new User();
        user.setUsername(username); user.setEmail(email.toLowerCase()); user.setPassword(encoder.encode(password));
        user.setFullName(fullName); user.setRole(role); user.setEnabled(true);
        users.save(user);
    }
}
