package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientBotStartCommand extends AbstractClientBotCommand {

    @Autowired
    SelectAddressClientBotCommand selectAddressClientBotCommand;



    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser user, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.CREATE_ORDER);
        return selectAddressClientBotCommand.process(update, botCommandDTO);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.START;
    }


}
