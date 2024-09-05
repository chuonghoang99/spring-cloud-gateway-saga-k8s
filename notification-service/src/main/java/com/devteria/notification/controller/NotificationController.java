package com.devteria.notification.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class NotificationController {

    @KafkaListener(topics = "notification-delivery")
    public void listenNotification(String event) {
        log.info("xxx: {} xxx", event);

//        emailService.sendEmail(SendEmailRequest.builder()
//                .to(Recipient.builder()
//                        .email(message.getRecipient())
//                        .build())
//                .subject(message.getSubject())
//                .htmlContent(message.getBody())
//                .build());
    }
}
