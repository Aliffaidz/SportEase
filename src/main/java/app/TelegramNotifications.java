package app;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
public class TelegramNotifications extends TelegramWebhookBot {

    private String tokenBot;
    private String botPath;
    private String botUsername;

    public TelegramNotifications(String token,String path,String botUsername){
        this.botPath = path;
        this.botUsername = botUsername;
        this.tokenBot = token;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update){
        log.info("send message");
        SendMessage sendMessage = new SendMessage();
        if(update.hasMessage()){
            String message = update.getMessage().getText();
            if(message.equalsIgnoreCase("VALID")){
                log.info("success send message");
                sendMessage.setChatId(update.getMessage().getChatId());
                sendMessage.setText("Merubah status booking menjadi valid");
            }else {
                sendMessage.setChatId(update.getMessage().getChatId());
                sendMessage.setText("Merbubah status booking menjad invalid");
            }
            return sendMessage;
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return this.botPath;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.tokenBot;
    }
}
