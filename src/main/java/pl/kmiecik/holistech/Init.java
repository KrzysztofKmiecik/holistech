package pl.kmiecik.holistech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kmiecik.holistech.email.application.port.GmailService;

@Component
class Init {

    private final GmailService emailService;


    @Autowired
    public Init(GmailService emailService) {
        this.emailService = emailService;

    }

  //  @EventListener(ApplicationReadyEvent.class)
    void sendEmail() {
        emailService.sendSimpleMessage("krzysztof_kmiecik@wp.pl", "test", "ciało");
    }


}
