package com.netas.cpaas.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationScheduler {

    @Scheduled(fixedRate = 1000)
    public void sendConAckForClients() {
        log.info("fixd rate 1000");
    }
}
