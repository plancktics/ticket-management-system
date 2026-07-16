package com.tics.ticket_management_system.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendTicketConfirmationEmail(String toEmail, String customerName, String eventTitle, String ticketCode, byte[] qrCodeBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message for attachments
            
            helper.setTo(toEmail);
            helper.setSubject("Your Ticket for " + eventTitle);
            
            String body = "Hello " + customerName + ",\n\n" +
                          "Thank you for your purchase. Your reservation has been successfully processed.\n\n" +
                          "Event: " + eventTitle + "\n" +
                          "Ticket Secure Code: " + ticketCode + "\n\n" +
                          "Attached to this email you will find your digital QR access ticket.\n\n" +
                          "See you there!\n" +
                          "Ticket Management System Support.";
            
            helper.setText(body);
            
            helper.addAttachment("access-qr.png", new ByteArrayResource(qrCodeBytes));
            
            mailSender.send(message);
            System.out.println("====== ASYNC EMAIL SENT SUCCESSFULLY TO: " + toEmail + " ======");
            
        } catch (Exception e) {
            System.err.println("Failed to send async ticket email to: " + toEmail);
            e.printStackTrace();
        }
    }
}