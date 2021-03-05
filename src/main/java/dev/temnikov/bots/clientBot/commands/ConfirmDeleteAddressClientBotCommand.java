package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConfirmDeleteAddressClientBotCommand extends AbstractClientBotCommand {

    private static final String CONFIRM = "Адрес успешно удален";
    private static final String ERROR = "Невозможно удалить данный адрес";

    @Autowired
    AddressService addressService;

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        long addressId = botUtils.parseId(botCommandDTO);
        Optional<Address> one = addressService.findOne(addressId);
        if (one.isEmpty()){
            return errorMessage(chatId);
        }
        boolean deleteAddress = facadeService.checkUserAndDeleteAddress(appUser, one.get());
        if (deleteAddress){
            return new SendMessage(chatId, CONFIRM).replyMarkup(clientBotKeyboardsFactory.getMenu());
        } else {
            return errorMessage(chatId);
        }
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.CONFIRM_DELETE_ADDRESS;
    }


    private SendMessage errorMessage(long chatId){
        return new SendMessage(chatId, ERROR).replyMarkup(clientBotKeyboardsFactory.getMenu());
    }
}
