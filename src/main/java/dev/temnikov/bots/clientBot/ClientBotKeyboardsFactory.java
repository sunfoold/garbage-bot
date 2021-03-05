package dev.temnikov.bots.clientBot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import dev.temnikov.bots.clientBot.constants.ClientBotButtons;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.domain.Address;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ClientBotKeyboardsFactory {
    private static final String MAKE_ORDER_ON_ADDRESS = "Сделать заказ по адресу ";
    private static final String DELETE_ADDRESS = "Удалить адрес %s";
    private static final String ADD_ADDRESS = "Добавить новый адрес";
    private static final String YES = "Да";
    private static final String NO = "Нет";
    private static final String INCREASE_BALANCE = "Пополнить баланс";
    private static final String INCREASE_BALANCE_LINK = "Перейти к пополнению баланса";
    private static final String URL_TO_INCREASE_BALANCE = "https://yoomoney.ru/to/4100116518599630";
    private static final InlineKeyboardButton cancelButton = new InlineKeyboardButton("Отмена")
    .callbackData(ClientBotCommandsPrefixes.MAIN_MENU);

    public InlineKeyboardMarkup getCreateOrderKeyboard(Address address) {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[1];

        buttons[0] =
            new InlineKeyboardButton(MAKE_ORDER_ON_ADDRESS + address.getStreetBuilding())
            .callbackData(ClientBotCommandsPrefixes.CREATE_ORDER + "_" + address.getId());
        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getAddressSetKeyboard(Address address) {
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[2][1];

        buttons[0][0] =
            new InlineKeyboardButton(MAKE_ORDER_ON_ADDRESS + address.getStreetBuilding())
            .callbackData(ClientBotCommandsPrefixes.CREATE_ORDER + "_" + address.getId());
        buttons[1][0] = cancelButton;
        return new InlineKeyboardMarkup(buttons);
    }

    private InlineKeyboardMarkup getActionOrCancelKeyboard(InlineKeyboardButton actionButton) {
        InlineKeyboardButton[][] lines = new InlineKeyboardButton[2][1];
        lines[0][0] = actionButton;
        lines[1][0] = cancelButton;
        return new InlineKeyboardMarkup(lines);
    }

    public InlineKeyboardMarkup getAddressKeyBoard(Set<Address> addresses) {
        InlineKeyboardButton[][] lines = new InlineKeyboardButton[addresses.size() + 1][1];
        int i = 1;
        lines[0][0] = new InlineKeyboardButton(ADD_ADDRESS).callbackData(ClientBotCommandsPrefixes.NEW_ADDRESS);
        for (Address address : addresses) {
            lines[i][0] =
                new InlineKeyboardButton(address.getStreetBuilding())
                .callbackData(ClientBotCommandsPrefixes.CREATE_ORDER + "_" + address.getId());
            i++;
        }
        return new InlineKeyboardMarkup(lines);
    }

    public InlineKeyboardMarkup getAddressSettingsKeyBoard(Set<Address> addresses) {
        InlineKeyboardButton[][] lines = new InlineKeyboardButton[addresses.size() + 1][1];
        int i = 1;
        lines[0][0] = new InlineKeyboardButton(ADD_ADDRESS).callbackData(ClientBotCommandsPrefixes.NEW_ADDRESS);
        for (Address address : addresses) {
            lines[i][0] =
                new InlineKeyboardButton(getDeleteAddressText(address))
                .callbackData(ClientBotCommandsPrefixes.DELETE_ADDRESS + "_" + address.getId());
            i++;
        }
        return new InlineKeyboardMarkup(lines);
    }

    public InlineKeyboardMarkup getConfirmationDeleteAddressKeyBoard(Address address) {
        InlineKeyboardButton[][] lines = new InlineKeyboardButton[2][1];
        lines[0][0] = new InlineKeyboardButton(YES).callbackData(ClientBotCommandsPrefixes.CONFIRM_DELETE_ADDRESS + "_" + address.getId());
        lines[1][0] = new InlineKeyboardButton(NO).callbackData(ClientBotCommandsPrefixes.ADDRESS_SETTINGS);
        return new InlineKeyboardMarkup(lines);
    }

    public InlineKeyboardMarkup getBalanceKeyBoard() {
        InlineKeyboardButton actionButton = new InlineKeyboardButton(INCREASE_BALANCE_LINK).url(URL_TO_INCREASE_BALANCE);
        return getActionOrCancelKeyboard(actionButton);
    }

    private String getDeleteAddressText(Address address) {
        return String.format(DELETE_ADDRESS, address.getStreetBuilding());
    }

    public ReplyKeyboardMarkup getMenu() {
        String[] line1 = { ClientBotButtons.NEW_ORDER };
        String[] line2 = { ClientBotButtons.ADDRESS_SETTINGS, ClientBotButtons.MY_BALANCE };
        String[] line3 = { ClientBotButtons.MY_ORDERS, ClientBotButtons.SETTINGS };
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(line1, line2, line3).oneTimeKeyboard(false);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getIncreaseBalanceUrlKeyboard() {
        InlineKeyboardButton actionButton = new InlineKeyboardButton(INCREASE_BALANCE)
        .callbackData(ClientBotCommandsPrefixes.INCREASE_BALANCE);
        return getActionOrCancelKeyboard(actionButton);
    }
}
