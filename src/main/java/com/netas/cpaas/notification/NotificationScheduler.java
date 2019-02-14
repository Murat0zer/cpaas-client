package com.netas.cpaas.notification;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    @Scheduled(fixedRate = 1000)
    public void sendConAckForClients() {

    }
}
