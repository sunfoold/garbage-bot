package dev.temnikov.bots.clientBot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import dev.temnikov.bots.clientBot.commands.ClientBotCommandsPrefixes;
import dev.temnikov.domain.Address;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ClientBotKeyboardsFactory {
    private static final String MAKE_ORDER_ON_ADDRESS = "Сделать заказ по адресу ";
    private static final String ADD_NEW_ADDRESS = "Сделать заказ по адресу ";

    public InlineKeyboardMarkup getCreateOrderKeyboard(Address address) {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[1];

        buttons[0] =
            new InlineKeyboardButton(MAKE_ORDER_ON_ADDRESS + address.getStreetBuilding())
            .callbackData(ClientBotCommandsPrefixes.CREATE_ORDER + "_" + address.getId());
        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getAddressKeyBoard(Set<Address> addresses) {
        InlineKeyboardButton[][] lines = new InlineKeyboardButton[addresses.size() + 1][1];
        int i = 1;
        lines[0][0] = new InlineKeyboardButton(ADD_NEW_ADDRESS).callbackData(ClientBotCommandsPrefixes.NEW_ADDRESS);
        for (Address address : addresses) {
            lines[i][0] =
                new InlineKeyboardButton(address.getStreetBuilding())
                .callbackData(ClientBotCommandsPrefixes.CREATE_ORDER + "_" + address.getId());
            i++;
        }
        return new InlineKeyboardMarkup(lines);
    }
}
