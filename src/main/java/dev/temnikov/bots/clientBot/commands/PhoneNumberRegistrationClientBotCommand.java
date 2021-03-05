package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.ApplicationUtils;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PhoneNumberRegistrationClientBotCommand extends AbstractClientBotCommand {

    private static final String NUMBER_IS_NOT_UNIQUE = "Такой номер уже зарегистрирован в системе. Введите другой номер";
    private static final String NUMBER_SET = "Номер %s успешно установлен";
    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        long chatId = botCommandDTO.getChatId();
        String phoneNumber = ApplicationUtils.formatPhoneNumber(botCommandDTO.getText());
        if (StringUtils.isEmpty(phoneNumber)){
            return sendRegistrationMessage(chatId);
        }
        Optional<AppUser> byUserPhone = appUserService.findByUserPhone(phoneNumber);
        if (byUserPhone.isPresent()){
            return new SendMessage(chatId, NUMBER_IS_NOT_UNIQUE);
        }
        appUser.setPhoneNumber(phoneNumber);
        appUserService.save(appUser);
        return new SendMessage(chatId, String.format(NUMBER_SET, phoneNumber))
            .replyMarkup(clientBotKeyboardsFactory.getMenu());
    }



    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.REGISTRATION;
    }
}
