package pl.kmiecik.holistech.email.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.kmiecik.holistech.config.CustomProperties;
import pl.kmiecik.holistech.email.application.port.GmailService;


@Service
class GmailUseCase implements GmailService {

    private final JavaMailSender emailSender;
    private final CustomProperties customProperties;

    @Autowired
    public GmailUseCase(JavaMailSender emailSender, CustomProperties customProperties) {
        this.emailSender = emailSender;
        this.customProperties = customProperties;
    }

    @Override
    public void sendSimpleMessage(final String to, final String subject, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(customProperties.getEmailsender());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}
