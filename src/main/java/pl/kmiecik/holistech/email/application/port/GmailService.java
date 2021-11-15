package pl.kmiecik.holistech.email.application.port;

public interface GmailService {
    void sendSimpleMessage(
            String to, String subject, String text);
}
