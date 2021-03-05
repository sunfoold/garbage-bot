package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.ClientBotKeyboardsFactory;
import dev.temnikov.bots.clientBot.constants.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.service.AddressService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddAddressClientBotCommand extends AbstractClientBotCommand {
    @Autowired
    AddressService addressService;

    @Autowired
    ClientBotKeyboardsFactory clientBotKeyboardsFactory;

    @Override
    protected AbstractSendRequest mainCommandLogic(Update update, AppUser user, BotCommandDTO botCommandDTO) {
        Long chatId = user.getTelegramChatId();
        Address address = new Address();
        final String addressStr = getAddressToSet(botCommandDTO);
        address.setStreetBuilding(addressStr);
        Set<Address> addresses = user.getAddresses();
        if (addresses == null) {
            addresses = new HashSet<>();
        }
        addresses.add(address);
        AppUser save = appUserService.save(user);
        Address address1 = save.getAddresses().stream().filter(ad -> addressStr.equals(ad.getStreetBuilding())).findFirst().get();
        clientBotExpectedCommands.deleteExpectedCommand(chatId);
        return new SendMessage(chatId, "Адрес установлен").replyMarkup(clientBotKeyboardsFactory.getAddressSetKeyboard(address1));
    }

    private String getAddressToSet(BotCommandDTO botCommandDTO) {
        String s = botUtils.parseAddress(botCommandDTO);
        if (StringUtils.isBlank(s)) {
            s = botCommandDTO.getText();
        }
        return s;
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.ADD_ADDRESS;
    }
}
