package pl.kmiecik.holistech.email.application;
/*

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.kmiecik.holistech.email.application.port.EmailService;
import pl.kmiecik.m6_aop_mail_homework.email.application.port.EmailUseCase;
import pl.kmiecik.m6_aop_mail_homework.film.application.port.FilmUseCase;
import pl.kmiecik.m6_aop_mail_homework.film.domain.Film;
*/
/*
@Aspect
@Component*/
class SendEmailAspect {
   /* private final EmailService emailService;
    private final FilmUseCase filmService;

    @Autowired
    public SendEmailAspect(EmailUseCase emailService, FilmUseCase filmService) {
        this.emailService = emailService;
        this.filmService = filmService;
    }

    @Value("${spring.mail.sendTo}")
    private String emailTo;

    @After("@annotation(pl.kmiecik.m6_aop_mail_homework.email.application.SendEmail)")
    private void sendEmailAfter() {
        String message = formatMassage();
        emailService.sendSimpleMessage(emailTo, "Film was added", message);
    }

    private String formatMassage() {
        int filmServiceSize=filmService.getAllFilms().size();
        Film lastElement=filmService.getAllFilms().get(filmServiceSize-1);
        return "Film was  added : "+lastElement.toString();

    }*/
}
