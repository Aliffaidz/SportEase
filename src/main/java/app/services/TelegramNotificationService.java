package app.services;
import app.TelegramNotifications;
import app.model.dto.Response;
import app.repositories.TelegramNotificationRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Data
@Service
public class TelegramNotificationService {

    private final TelegramNotificationRepository telegramNotificationRepository;

    public TelegramNotificationService(TelegramNotificationRepository telegramNotificationRepository){
        this.telegramNotificationRepository = telegramNotificationRepository;
    }


    public void

    // add
    //delete

}
