package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.web.dto.response.EmailDetailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;

@Service
@Slf4j
@RequiredArgsConstructor

public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(EmailDetailDto emailDetailDto) {
        // Creating a mime message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        // Try block to check for exception
        try {
            // Create mime message helper to send mail
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetailDto.getRecipient());
            mimeMessageHelper.setText(emailDetailDto.getMsgBody(), true); // when set true this mean you can custom this text by html
            mimeMessageHelper.setSubject(emailDetailDto.getSubject());
            mailSender.send(mimeMessage);
            log.info("Send mail SUCCESS!");

        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("Send mail FAIL!");
            log.trace("Make sure that your mail body is input and other mail property it's too!");
        }
    }

}