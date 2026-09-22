package vn.iotstar.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender sender;
    private final boolean enabled;
    private final String from;
    public MailService(JavaMailSender sender, @Value("${app.mail.enabled:false}") boolean enabled,
                       @Value("${app.mail.from}") String from) {
        this.sender = sender; this.enabled = enabled; this.from = from;
    }
    public void send(String to, String subject, String body) {
        if (!enabled) { log.info("DEMO MAIL to={} subject={} body={}", to, subject, body); return; }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); message.setTo(to); message.setSubject(subject); message.setText(body);
        sender.send(message);
    }
}
