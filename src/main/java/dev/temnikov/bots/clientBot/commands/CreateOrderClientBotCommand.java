package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderClientBotCommand extends AbstractClientBotCommand {
    private static final String SEND_PHOTO =
        "Для завершения создания заказа, выставите мусорный пакет у входной двери и пришлите фотографию в ответном сообщении";

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser user, BotCommandDTO botCommandDTO) {
        long chatID = botCommandDTO.getChatId();
        long addressID = botUtils.parseId(botCommandDTO);
        facadeService.createOrder(chatID, addressID);
        clientBotExpectedCommands.addExpectedCommand(chatID, ClientBotCommandsPrefixes.UPLOAD_PHOTO);
        return new SendMessage(chatID, SEND_PHOTO);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.CREATE_ORDER;
    }
}
