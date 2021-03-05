package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.BotUtils;
import dev.temnikov.bots.clientBot.ClientBotCommand;
import dev.temnikov.bots.clientBot.ClientBotExpectedCommands;
import dev.temnikov.bots.clientBot.ClientBotKeyboardsFactory;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.AppUser;
import dev.temnikov.service.AppUserService;
import dev.temnikov.service.ApplicationFacadeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class AbstractClientBotCommand implements ClientBotCommand {
    @Autowired
    ClientBotExpectedCommands clientBotExpectedCommands;

    @Autowired
    ApplicationFacadeService facadeService;

    @Autowired
    ClientBotKeyboardsFactory clientBotKeyboardsFactory;

    @Autowired
    BotUtils botUtils;

    @Autowired
    AppUserService appUserService;


    private static final String NEED_PHONE_NUMBER = "Для работы с ботом введите свой номер телефона в формате +7ХХХХХХХХХХ";
    private static final String SELECT_ADDRESS = "У вас не сохраннено ни одного адреса. Добавьте ваш адрес в ответном сообщении";

    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO){
        long chatId = botCommandDTO.getChatId();
        Optional<AppUser> byTelegramChatId = appUserService.findByTelegramChatId(chatId);
        if (byTelegramChatId.isEmpty()){
            byTelegramChatId = Optional.of(facadeService.createNewUser(chatId));
        }
        AppUser user = byTelegramChatId.get();
        if (!hasUserPhoneNumber(user) && !isCommandToRegisterAddress(botCommandDTO)){
            return sendRegistrationMessage(chatId);
        } 
        if (user.getAddresses()== null || user.getAddresses().isEmpty()){
            clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.ADD_ADDRESS);
            return new SendMessage(chatId, SELECT_ADDRESS);
        }
        return mainCommandLogic(update, user, botCommandDTO);
        
    }

    private boolean isCommandToRegisterAddress(BotCommandDTO botCommandDTO) {
        String text = botCommandDTO.getText();
        if (StringUtils.isEmpty(text)){
            return false;
        }
        return text.contains(ClientBotCommandsPrefixes.REGISTRATION);
    }

    protected abstract AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO);

    public boolean hasUserPhoneNumber(AppUser appUser){
        return (appUser!=null) && (appUser.getPhoneNumber()!=null) && !(" ".equals(appUser.getPhoneNumber()));
    }

    public SendMessage sendRegistrationMessage (long chatId){
        clientBotExpectedCommands.addExpectedCommand(chatId, ClientBotCommandsPrefixes.REGISTRATION);
        return new SendMessage(chatId, NEED_PHONE_NUMBER);
    }
}
