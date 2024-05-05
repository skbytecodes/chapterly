package com.chapterly.controller;

import com.chapterly.dto.EmailRequestDto;
import com.chapterly.serviceImpl.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    @Autowired
    private EmailSenderService emailSenderService;


    @PostMapping("/letsTalk")
    public ResponseEntity<?> sendMail(@RequestBody EmailRequestDto emailRequest){
        boolean isMailSent = emailSenderService.sendSimpleEmail(emailRequest);
        if(isMailSent)
            return new ResponseEntity<>("MAIL SENT SUCCESSFULLY", HttpStatus.OK);
        else
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestParam("file") MultipartFile file,
                                         @RequestParam("toEmail") String toEmail,
                                         @RequestParam("subject") String subject,
                                         @RequestParam("body") String body) throws MessagingException {

        return emailSenderService.sendMailWithAttachment(toEmail,body,subject,file);
    }

}
