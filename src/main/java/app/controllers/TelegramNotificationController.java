package app.controllers;

import app.TelegramNotifications;
import app.services.TelegramNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
public class TelegramNotificationController {

    private TelegramNotificationService telegramNotificationService;

    public TelegramNotificationController(TelegramNotificationService telegramNotificationService) {
        this.telegramNotificationService = telegramNotificationService;
    }

    @PostMapping(path = "/notification/webhook")
    public BotApiMethod<?> updateMessage(@RequestBody Update update){
        log.info("send new message");
        TelegramNotifications currentBot = telegramNotificationService.getBot("/notification/webhook");
        return currentBot.onWebhookUpdateReceived(update);
    }


}
