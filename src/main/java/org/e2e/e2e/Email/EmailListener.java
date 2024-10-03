package org.e2e.e2e.Email;

import org.e2e.e2e.Email.EmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void handleEmailEvent(EmailEvent event) {
        // Enviar el correo electrónico cuando ocurra un evento
        emailService.sendSimpleMessage(event.getTo(), event.getSubject(), event.getText());
    }
}
