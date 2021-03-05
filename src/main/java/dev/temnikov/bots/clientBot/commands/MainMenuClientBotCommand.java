package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import org.springframework.stereotype.Component;

@Component
public class MainMenuClientBotCommand extends AbstractClientBotCommand {
    private static final String MAIN_TEXT =
        "Мы работаем в тестовом режиме! \n\n" + "Пожалуйста все вопросы, предложения и пожелания направляйте в поддержку";

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        return new SendMessage(chatId, MAIN_TEXT).replyMarkup(clientBotKeyboardsFactory.getMenu());
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.MAIN_MENU;
    }
}
