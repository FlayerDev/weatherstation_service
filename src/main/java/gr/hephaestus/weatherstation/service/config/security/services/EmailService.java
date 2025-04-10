package gr.hephaestus.weatherstation.service.config.security.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(String toEmail, String activationLink) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🚀 Activate Your Hephaestus Weather Station Account");

            String htmlContent = """
                <html>
                    <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2>Welcome to Our Weather Station! 🎉</h2>
                        <p>Click the button below to activate your account:</p>
                        <p style="margin-top: 20px;">
                            <a href="%s" 
                               style="display: inline-block; padding: 10px 20px; background-color: #4CAF50; 
                                      color: white; text-decoration: none; border-radius: 5px;" >
                                Activate My Account
                            </a>
                        </p>
                        <p>If the button doesn't work, copy and paste this link into your browser:</p>
                        <p><a href="%s">%s</a></p>
                        <hr />
                        <small>This link will expire in 24 hours. If you didn’t sign up, feel free to ignore this email.</small>
                    </body>
                </html>
                """.formatted(activationLink, activationLink, activationLink);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log properly in production
        }
    }
}

