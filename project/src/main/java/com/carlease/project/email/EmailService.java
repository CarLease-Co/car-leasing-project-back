package com.carlease.project.email;

import jakarta.annotation.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailService {

    // CREDENTIALS ->
    private static final String email = "teamcarlease@gmail.com";
    private static final String password = "wayn adop zxtq srur";
    // <- CREDENTIALS

    private static JavaMailSenderImpl sender;

    public static void sendEmail(String subject, String messages, @Nullable String recipient) {
        sender = sender != null ? sender : initSender();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("teamcarlease@gmail.com");
        message.setTo(recipient != null ? recipient : "teamcarlease@gmail.com");
        message.setSubject(subject);
        message.setText(messages);

        sender.send(message);
    }

    private static JavaMailSenderImpl initSender() {
        sender = new JavaMailSenderImpl();
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername(email);
        sender.setPassword(password);
        sender.getJavaMailProperties().put("mail.smtp.auth", "true");
        sender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");

        return sender;
    }

}