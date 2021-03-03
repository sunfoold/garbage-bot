package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import dev.temnikov.service.AppUserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientBotStartCommand extends AbstractClientBotCommand {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    SelectAddressClientBotCommand selectAddressClientBotCommand;

    private static final String SELECT_ADDRESS = "Укажите адрес, с которого нам надо забрать мусор";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        Optional<AppUser> optUser = appUserService.findByTelegramChatId(chatId);
        if (optUser.isEmpty()) {
            facadeService.createNewUser(chatId);
            clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.ADD_ADDRESS);
            return new SendMessage(chatId, SELECT_ADDRESS);
        }
        clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.CREATE_ORDER);
        return selectAddressClientBotCommand.process(update, botCommandDTO);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.START;
    }
}
