package dev.temnikov.bots;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface BotInterface {
    String getDefaultCommand();

    ExpectedCommands getExpectedCommands();

    BotCommand getCommand(String command);

    TelegramBot getBot();

    void sendMessage(AbstractSendRequest sendMessage);
}
