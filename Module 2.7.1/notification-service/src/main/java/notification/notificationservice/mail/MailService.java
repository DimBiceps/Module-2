package notification.notificationservice.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MailService {
  private static final Logger log = LoggerFactory.getLogger(MailService.class);

  private final JavaMailSender sender;

  public MailService(JavaMailSender sender) {
    this.sender = sender;
  }

  @CircuitBreaker(name = "mailService", fallbackMethod = "fallbackSend")
  public void send(String to, String subject, String text) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText(text);
    sender.send(msg);
  }

  // fallback-метод для circuit breaker
  @SuppressWarnings("unused")
  void fallbackSend(String to, String subject, String text, Throwable ex) {
    log.error("Failed to send email to {}. Subject: {}. Reason: {}", to, subject, ex.getMessage());
    // здесь можно: сохранить в БД/очередь на ретраи и т.п.
  }
}
