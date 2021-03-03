package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Courier;
import dev.temnikov.service.CourierService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CourierService courierService;

    @Autowired
    AvailableOrdersCourierBotCommand availableOrdersCourierBotCommand;

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        Optional<Courier> courierOpt = courierService.findByTelegramChatId(chatId);
        if (courierOpt.isEmpty()) {
            applicationFacadeService.createNewCourier(chatId);
        }
        botCommandDTO.setText(CourierBotCommandsPrefixes.AVAILABLE_ORDERS + "_" + 1);
        return availableOrdersCourierBotCommand.process(update, botCommandDTO);
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.START;
    }
}
