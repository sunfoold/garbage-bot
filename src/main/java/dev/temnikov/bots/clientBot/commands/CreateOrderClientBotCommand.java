package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.cache.CacheService;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.domain.Order;
import dev.temnikov.domain.enumeration.OrderStatus;
import dev.temnikov.service.AddressService;
import dev.temnikov.service.AppUserService;
import dev.temnikov.service.ApplicationFacadeService;
import dev.temnikov.service.OrderService;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderClientBotCommand extends AbstractClientBotCommand {
    private static final String SEND_PHOTO =
        "Для завершения создания заказа, выставите мусорный пакет у входной двери и пришлите фотографию в ответном сообщении";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
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
