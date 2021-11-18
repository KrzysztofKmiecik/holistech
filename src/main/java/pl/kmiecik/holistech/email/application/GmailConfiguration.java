package pl.kmiecik.holistech.email.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.kmiecik.holistech.config.CustomProperties;

import java.util.Properties;


@Configuration
@EnableConfigurationProperties(CustomProperties.class)
class GmailConfiguration {

    private final CustomProperties customProperties;

    @Autowired
    public GmailConfiguration(CustomProperties customProperties) {
        this.customProperties = customProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.aptiv.com");
        mailSender.setPort(25);
        mailSender.setUsername(customProperties.getEmailsender());
        mailSender.setPassword(customProperties.getEmailpassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
