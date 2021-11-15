package pl.kmiecik.holistech;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
class HolistechApplication {

    public static void main(String[] args) {
        SpringApplication.run(HolistechApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void getProp(){
        System.out.println("hasło "+propPass);
        System.out.println("login "+propLogin);
    }

    @Value("${myproxy.login}")
    private String propLogin;

    @Value("${myproxy.password}")
    private String propPass;
}
