package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AddressSettingsClientBotCommand extends AbstractClientBotCommand {

    private static final String ADDRESSES = "Нажмите на первую кнопку для добавления нового адреса " +
        "или же выберете адрес, который хотите удалить";

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        InlineKeyboardMarkup addressKeyBoard =
            clientBotKeyboardsFactory.getDeleteAddressKeyBoard(appUser.getAddresses());
        return new SendMessage(chatId, ADDRESSES).replyMarkup(addressKeyBoard);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.ADDRESS_SETTINGS;
    }
}
