package notificationservice;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.mail.host=localhost",
    "spring.mail.port=3026"
})
class NotifyApiIT {

  static GreenMail smtp;

  @BeforeAll
  static void startSmtp() {
    smtp = new GreenMail(new ServerSetup(3026, null, "smtp"));
    smtp.start();
  }

  @AfterAll
  static void stopSmtp() { smtp.stop(); }

  @Autowired WebTestClient webClient;

  @Test
  void postNotify_sendsEmail() throws Exception {
    webClient.post().uri("/api/notify")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("""
          {"email":"u@example.com","subject":"Hello","message":"Body"}
        """)
        .exchange()
        .expectStatus().isAccepted();

    smtp.waitForIncomingEmail(1);
    MimeMessage[] messages = smtp.getReceivedMessages();
    assertThat(messages).hasSize(1);
    assertThat(messages[0].getSubject()).isEqualTo("Hello");
  }
}
