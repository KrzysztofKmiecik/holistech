package pl.kmiecik.holistech.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "fixture.service.usecase.email")
public class CustomFixturesProperties {
    String sender;
    String password;
    String receiver;
}
