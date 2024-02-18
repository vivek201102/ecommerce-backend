package org.vivek.ecommerce.Services;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    public void sendEmail(SimpleMailMessage simpleMailMessage);
}
