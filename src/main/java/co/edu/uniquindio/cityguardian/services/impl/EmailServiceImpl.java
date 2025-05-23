package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.dto.EmailDTO;
import co.edu.uniquindio.cityguardian.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Override
    @Async
    public void enviarEmail(EmailDTO emailDTO) throws Exception {
        Mailer mailer = MailerBuilder
                .withSMTPServer(mailHost, mailPort, mailUsername, mailPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        mailer.sendMail(EmailBuilder.startingBlank()
                .from(mailUsername)
                .to(emailDTO.getDestinatario())
                .withSubject(emailDTO.getAsunto())
                .withHTMLText(emailDTO.getCuerpo())
                .buildEmail());
    }
}