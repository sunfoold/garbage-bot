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
public class ShowBalanceClientBotCommand extends AbstractClientBotCommand {
    private static final String BALANCE = "Ваш баланс = %d рублей";

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        InlineKeyboardMarkup balanceKeyBoard = clientBotKeyboardsFactory.getBalanceKeyBoard();
        return new SendMessage(chatId, generateText(appUser)).replyMarkup(balanceKeyBoard);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.BALANCE;
    }

    private String generateText(AppUser user) {
        return String.format(BALANCE, user.getBalance());
    }
}
