package vn.iotstar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "otp_tokens") @Getter @Setter @NoArgsConstructor
public class OtpToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 150) private String email;
    @Column(nullable = false, length = 20) private String type;
    @Column(nullable = false) private String codeHash;
    @Column(nullable = false) private LocalDateTime expiresAt;
    @Column(nullable = false) private boolean used;
}
