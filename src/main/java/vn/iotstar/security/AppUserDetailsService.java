package vn.iotstar.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.iotstar.repository.UserRepository;

@Service public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    private final int example;
    public AppUserDetailsService(UserRepository users, @Value("${app.example:3}") int example) {
        this.users = users; this.example = example;
    }
    @Override public UserDetails loadUserByUsername(String login) {
        return (example == 1 ? users.findByEmailIgnoreCase(login)
                : users.findByUsernameIgnoreCaseOrEmailIgnoreCase(login, login))
                .map(AppPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }
}
