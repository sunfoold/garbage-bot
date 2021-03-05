package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import org.springframework.stereotype.Component;

@Component
public class IncreaseBalanceClientBotCommand extends AbstractClientBotCommand {
    private static final String MESSAGE =
        "Для того чтобы пополнить баланс перейдите по ссылке ниже и оформите перевод. \n" +
        "<code><b> Обязательно в поле \"комментарий \" укажите номер телефона, привязанный к данному аккаунту: <i> %s </i> в точно таком же формате</b></code>.\n" +
        "Деньги поступят на счет в течение 5 минут";

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        InlineKeyboardMarkup increaseBalanceUrlKeyboard = clientBotKeyboardsFactory.getIncreaseBalanceUrlKeyboard();
        return new SendMessage(botCommandDTO.getChatId(), getMessageText(appUser)).replyMarkup(increaseBalanceUrlKeyboard);
    }

    private String getMessageText(AppUser appUser) {
        return String.format(MESSAGE, appUser.getPhoneNumber());
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.INCREASE_BALANCE;
    }
}
