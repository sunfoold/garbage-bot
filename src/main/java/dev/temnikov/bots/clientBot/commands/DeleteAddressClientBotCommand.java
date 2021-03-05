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
public class DeleteAddressClientBotCommand extends AbstractClientBotCommand {

    @Autowired
    AddressService addressService;

    private static final String CONFIRM = "Вы уверены, что хотите удалить адрес %s ?";
    private static final String ERROR = "Невозможно удалить данный адрес";


    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser appUser, BotCommandDTO botCommandDTO) {
        long addressId = botUtils.parseId(botCommandDTO);
        Optional<Address> one = addressService.findOne(addressId);
        Long chatId = botCommandDTO.getChatId();
        if (one.isEmpty()){
            return errorMessage(chatId);
        }
        boolean isPossibleToDelete = facadeService.checkUserAddress(appUser, addressId);
        if (isPossibleToDelete){
            return new SendMessage(chatId, confirmationMessage(one.get()))
                .replyMarkup(clientBotKeyboardsFactory.getConfirmationDeleteAddressKeyBoard(one.get()));
        } else {
            return errorMessage(chatId);
        }
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.DELETE_ADDRESS;
    }

    private SendMessage errorMessage(long chatId){
        return new SendMessage(chatId, ERROR).replyMarkup(clientBotKeyboardsFactory.getMenu());
    }

    private String confirmationMessage(Address address){
        return String.format(CONFIRM, address.getStreetBuilding());
    }
}
