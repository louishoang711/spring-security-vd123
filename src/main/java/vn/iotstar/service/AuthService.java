package vn.iotstar.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.RegisterForm;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Service public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final OtpService otp;
    public AuthService(UserRepository users, RoleRepository roles, PasswordEncoder encoder, OtpService otp) {
        this.users = users; this.roles = roles; this.encoder = encoder; this.otp = otp;
    }
    @Transactional public void register(RegisterForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) throw new IllegalArgumentException("Passwords do not match");
        if (users.existsByEmailIgnoreCase(form.getEmail()) || users.existsByUsernameIgnoreCase(form.getUsername()))
            throw new IllegalArgumentException("Username or email already exists");
        User user = new User();
        user.setUsername(form.getUsername().trim()); user.setEmail(form.getEmail().trim().toLowerCase());
        user.setFullName(form.getFullName().trim()); user.setPassword(encoder.encode(form.getPassword()));
        user.setRole(roles.findByName("ROLE_USER").orElseThrow()); user.setEnabled(false);
        users.save(user);
        otp.send(user.getEmail(), "REGISTER");
    }
    @Transactional public boolean verify(String email, String code) {
        if (!otp.consume(email, "REGISTER", code)) return false;
        User user = users.findByEmailIgnoreCase(email).orElseThrow(); user.setEnabled(true);
        return true;
    }
    public void requestReset(String email) {
        if (users.existsByEmailIgnoreCase(email)) otp.send(email, "RESET");
    }
    @Transactional public boolean reset(String email, String code, String password) {
        if (password.length() < 8 || !otp.consume(email, "RESET", code)) return false;
        User user = users.findByEmailIgnoreCase(email).orElseThrow();
        user.setPassword(encoder.encode(password));
        return true;
    }
}
