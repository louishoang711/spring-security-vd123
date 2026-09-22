package vn.iotstar.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.OtpToken;
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailIgnoreCaseAndTypeAndUsedFalseOrderByIdDesc(String email, String type);
    void deleteByEmailIgnoreCaseAndType(String email, String type);
}
