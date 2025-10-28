package notification.notificationservice.web;

import notification.notificationservice.mail.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

record NotifyRequest(@Email @NotBlank String email,
                     @NotBlank String subject,
                     @NotBlank String message) {}

@RestController
@RequestMapping("/api/notify")
public class NotifyController {
  private final MailService mail;

  public NotifyController(MailService mail) { this.mail = mail; }

  @PostMapping @ResponseStatus(HttpStatus.ACCEPTED)
  public void send(@RequestBody NotifyRequest req) {
    mail.send(req.email(), req.subject(), req.message());
  }
}
