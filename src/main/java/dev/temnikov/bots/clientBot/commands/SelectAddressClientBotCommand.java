package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.service.AppUserService;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectAddressClientBotCommand extends AbstractClientBotCommand {
    @Autowired
    private AppUserService appUserService;

    private static final String NO_ADDRESSES = "У вас не сохраннено ни одного адреса. Добавьте ваш адрес в ответном сообщении";
    private static final String CHOOSE_ADDRESS = "Выберите адрес, с которого нужно выбросить мусор";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        AppUser appUser = appUserService.findByTelegramChatId(chatId).get();
        Set<Address> addresses = appUser.getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.ADD_ADDRESS);
            return new SendMessage(chatId, NO_ADDRESSES);
        } else {
            clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.CREATE_ORDER);
            return new SendMessage(chatId, CHOOSE_ADDRESS).replyMarkup(clientBotKeyboardsFactory.getAddressKeyBoard(addresses));
        }
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.SELECT_ADDRESS;
    }
}
