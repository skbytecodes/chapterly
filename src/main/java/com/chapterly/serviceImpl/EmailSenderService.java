package com.chapterly.serviceImpl;


import com.chapterly.dto.EmailRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendSimpleEmail(EmailRequestDto emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailRequest.getFromEmail());
        message.setTo("who.sushill@gmail.com");
        message.setText(emailRequest.getBody());
        message.setSubject("Message Received: Let's Talk Form");
        try {
            mailSender.send(message);
            System.out.println("mail sent...");
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public String sendMailWithAttachment(String toEmail, String body, String subject, MultipartFile file) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("skbytecodes@gmail.com");
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setTo("");
            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.addAttachment(file.getOriginalFilename(), file);
            mailSender.send(mimeMessage);
            System.out.println("mail sent...");
        } catch (Exception e) {
            return "Something Went Wrong";
        }
        return "email sent...";
    }
}
