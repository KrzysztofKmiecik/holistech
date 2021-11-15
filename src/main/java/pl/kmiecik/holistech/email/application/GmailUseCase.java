package pl.kmiecik.holistech.email.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.kmiecik.holistech.email.application.port.GmailService;


/**
 * https://www.baeldung.com/spring-email
 */

@Service
class GmailUseCase implements GmailService {

    private final JavaMailSender emailSender;


    @Autowired
    public GmailUseCase(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Value("${fixture.service.usecase.sender}")
    private String sender;
    @Override
    public void sendSimpleMessage(final String to, final String subject, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


}
