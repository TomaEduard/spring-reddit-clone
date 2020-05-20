package com.example.springredditclone.service;

import com.example.springredditclone.exception.SpringRedditException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    void sendMail(String recipient, String message) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(recipient);
            messageHelper.setSubject("Account Activation");
            messageHelper.setText(mailContentBuilder.build(message));
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!");
        } catch (MailException e) {
            throw new SpringRedditException("Exception occurred when sending mail to " + recipient);
        }

    }

}
