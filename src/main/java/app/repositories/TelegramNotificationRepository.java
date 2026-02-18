package app.repositories;

import app.entities.TelegramNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramNotificationRepository extends JpaRepository<TelegramNotification, Integer> {


}
