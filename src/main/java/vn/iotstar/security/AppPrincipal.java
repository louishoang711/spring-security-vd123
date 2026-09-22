package vn.iotstar.security;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.iotstar.entity.User;

public record AppPrincipal(Long id, String username, String email, String password, String fullName,
                           String imageUrl, String role, boolean enabled) implements UserDetails, Serializable {
    public static AppPrincipal from(User user) {
        return new AppPrincipal(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
                user.getFullName(), user.getImageUrl(), user.getRole().getName(), user.isEnabled());
    }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isEnabled() { return enabled; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getImageUrl() { return imageUrl; }
    public String getRole() { return role; }
    public Long getId() { return id; }
}
