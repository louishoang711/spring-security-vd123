package vn.iotstar.service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.entity.OtpToken;
import vn.iotstar.repository.OtpTokenRepository;

@Service public class OtpService {
    private final OtpTokenRepository tokens;
    private final PasswordEncoder encoder;
    private final SecureRandom random = new SecureRandom();
    private final MailService mail;
    public OtpService(OtpTokenRepository tokens, PasswordEncoder encoder, MailService mail) {
        this.tokens = tokens; this.encoder = encoder; this.mail = mail;
    }
    @Transactional public void send(String email, String type) {
        tokens.deleteByEmailIgnoreCaseAndType(email, type);
        String code = "%06d".formatted(random.nextInt(1_000_000));
        OtpToken token = new OtpToken();
        token.setEmail(email.toLowerCase()); token.setType(type); token.setCodeHash(encoder.encode(code));
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        tokens.save(token);
        mail.send(email, "Your " + type + " code", "Verification code: " + code + " (valid 10 minutes)");
    }
    @Transactional public boolean consume(String email, String type, String code) {
        var found = tokens.findTopByEmailIgnoreCaseAndTypeAndUsedFalseOrderByIdDesc(email, type);
        if (found.isEmpty()) return false;
        var token = found.get();
        if (token.getExpiresAt().isBefore(LocalDateTime.now()) || !encoder.matches(code, token.getCodeHash())) return false;
        token.setUsed(true);
        return true;
    }
}
