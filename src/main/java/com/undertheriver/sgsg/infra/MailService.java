package com.undertheriver.sgsg.infra;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MailService {

    public static final String DEFAULT_PATH = ".";
    private static final String SGSG_MAIL = "sgsg.space@gmail.com";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private final JavaMailSender javaMailSender;
    private final TemplateService templateService;

    public void send(String title, String recipientEmail, Map<String, Object> models) {
        send(DEFAULT_PATH, title, recipientEmail, models);
    }

    public void send(String templatePath, String title, String recipientEmail, Map<String, Object> models) {
        String template = templateService.createTemplate(templatePath, models);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, DEFAULT_ENCODING);
            mimeMessageHelper.setFrom(SGSG_MAIL);
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(template, true);
        } catch (MessagingException e) {
            throw new IllegalArgumentException();
        }

        javaMailSender.send(message);
    }
}
