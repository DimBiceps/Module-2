package notificationservice;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "user.events")
@TestPropertySource(properties = {
    "kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.mail.host=localhost",
    "spring.mail.port=3025"
})
class UserEventEmailIT {

  static GreenMail smtp;

  @BeforeAll
  static void startSmtp() {
    smtp = new GreenMail(new ServerSetup(3025, null, "smtp"));
    smtp.start();
  }

  @AfterAll
  static void stopSmtp() { smtp.stop(); }

  @Autowired KafkaTemplate<String, String> template;

  @Test
  void whenUserCreated_event_sendsEmail() throws Exception {
    String payload = """
      {"type":"USER_CREATED","userId":42,"email":"test@example.com","occurredAt":"2025-01-01T00:00:00Z"}
    """;
    template.send("user.events", "42", payload);

    // ждём письмо
    smtp.waitForIncomingEmail(1);
    MimeMessage[] messages = smtp.getReceivedMessages();
    assertThat(messages).hasSize(1);
    assertThat(messages[0].getAllRecipients()[0].toString()).isEqualTo("test@example.com");
    assertThat(messages[0].getSubject()).contains("Аккаунт создан");
  }
}
