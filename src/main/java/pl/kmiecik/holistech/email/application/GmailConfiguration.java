package pl.kmiecik.holistech.email.application;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * https://www.baeldung.com/spring-email
 */


@Configuration
class GmailConfiguration {

    @Value("${fixture-service-usecase.emailFrom}")
    private String emailFrom;

    @Value("${fixture-service-usecase.emailFromPass}")
    private String emailFromPass;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.aptiv.com");
        mailSender.setPort(25);
        mailSender.setUsername(emailFrom);
        mailSender.setPassword(emailFromPass);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
