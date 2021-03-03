package dev.temnikov.bots;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    TelegramBot clientBot;
    TelegramBot courierBot;

    private LinkedBlockingQueue<AbstractSendRequest> clientBotQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<AbstractSendRequest> courierBotQueue = new LinkedBlockingQueue<>();

    public void registerCourierBot(TelegramBot courierBot) {
        this.courierBot = courierBot;
    }

    public void registerClientBot(TelegramBot clientBot) {
        this.clientBot = clientBot;
    }

    public void addMessageToClientBot(AbstractSendRequest sendMessage) {
        try {
            clientBotQueue.put(sendMessage);
        } catch (Exception e) {}
    }

    public void addMessageToCourierBot(AbstractSendRequest sendMessage) {
        try {
            courierBotQueue.put(sendMessage);
        } catch (Exception e) {}
    }

    @Scheduled(fixedDelay = 100)
    private void sendMessageToCourier() {
        while (true) {
            if (!courierBotQueue.isEmpty()) {
                courierBot.execute(courierBotQueue.poll());
            }
        }
    }

    @Scheduled(fixedDelay = 100)
    private void sendMessageToClient() {
        while (true) {
            if (!clientBotQueue.isEmpty()) {
                clientBot.execute(clientBotQueue.poll());
            }
        }
    }
}
