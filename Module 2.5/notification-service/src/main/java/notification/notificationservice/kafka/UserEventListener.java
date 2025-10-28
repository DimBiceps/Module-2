package notification.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import notification.notificationservice.mail.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
  private final ObjectMapper objectMapper;
  private final MailService mail;
  private final String siteName;

  public UserEventListener(ObjectMapper objectMapper, MailService mail,
                           @Value("${app.site-name:pobedaBydetZaNami}") String siteName) {
    this.objectMapper = objectMapper;
    this.mail = mail;
    this.siteName = siteName;
  }

  @KafkaListener(topics = "${app.kafka.user-topic:user.events}", containerFactory = "kafkaListenerContainerFactory")
  public void onMessage(String payload) throws Exception {
    UserEvent e = objectMapper.readValue(payload, UserEvent.class);
    if ("USER_CREATED".equals(e.type())) {
      mail.send(e.email(), "Аккаунт создан",
          "Здравствуйте! Ваш аккаунт на сайте " + siteName + " был успешно создан.");
    } else if ("USER_DELETED".equals(e.type())) {
      mail.send(e.email(), "Аккаунт удалён",
          "Здравствуйте! Ваш аккаунт был удалён.");
    }
  }
}
