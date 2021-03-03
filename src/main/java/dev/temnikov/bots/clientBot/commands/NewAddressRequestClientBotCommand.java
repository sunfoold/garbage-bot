package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import org.springframework.stereotype.Component;

@Component
public class NewAddressRequestClientBotCommand extends AbstractClientBotCommand {
    public static final String ENTER_ADDRESS = "Введите новый адрес";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.ADD_ADDRESS);
        return new SendMessage(chatId, ENTER_ADDRESS);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.NEW_ADDRESS;
    }
}
