package pl.kmiecik.holistech.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "myproxy")
public class CustomProxyProperties {
    String login;
    String password;
}
