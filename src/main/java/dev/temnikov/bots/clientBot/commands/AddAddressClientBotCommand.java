package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.clientBot.ClientBotKeyboardsFactory;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.service.AddressService;
import dev.temnikov.service.AppUserService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddAddressClientBotCommand extends AbstractClientBotCommand {
    @Autowired
    AppUserService appUserService;

    @Autowired
    AddressService addressService;

    @Autowired
    ClientBotKeyboardsFactory clientBotKeyboardsFactory;

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        Optional<AppUser> byTelegramChatId = appUserService.findByTelegramChatId(chatId);
        AppUser appUser = byTelegramChatId.get();
        Address address = new Address();
        address.setStreetBuilding(botCommandDTO.getText());
        Set<Address> addresses = appUser.getAddresses();
        if (addresses == null) {
            addresses = new HashSet<>();
        }
        addresses.add(address);
        AppUser save = appUserService.save(appUser);
        Address address1 = save
            .getAddresses()
            .stream()
            .filter(ad -> botCommandDTO.getText().equals(ad.getStreetBuilding()))
            .findFirst()
            .get();
        clientBotExpectedCommands.deleteExpectedCommand(chatId);
        return new SendMessage(chatId, "Адрес установлен").replyMarkup(clientBotKeyboardsFactory.getCreateOrderKeyboard(address1));
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.ADD_ADDRESS;
    }
}
