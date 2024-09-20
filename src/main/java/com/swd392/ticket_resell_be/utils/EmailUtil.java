package com.swd392.ticket_resell_be.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    private void send(String email, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendVerifyEmail(String email, String username, String token) throws MessagingException {
        String subject = "Verify your email";
        String url = "http://localhost:8081/auth/verify-email?token=" + token;
        String content = """
                <div>
                  Dear %s,<br>
                  If you want to verify your email, please
                  <a href="%s" target="_blank">Click here</a>
                  <br><br>
                  <div style="border-top:1px solid #eaeaea; padding-top:10px;">
                    Best Regards,<br>
                    Ticket Resell team<br>
                  </div>
                </div>
                """.formatted(username, url);
        send(email, subject, content);
    }
}
