package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import dev.temnikov.service.CourierService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCourierBotCommand extends AbstractCourierBotCommand {
    private static final String GO_TO_ACTIVE_ORDER = "Перейти к активному заказу";

    @Autowired
    CourierService courierService;

    @Autowired
    AvailableOrdersCourierBotCommand availableOrdersCourierBotCommand;

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        Optional<Courier> courierOptional = courierService.findByTelegramChatId(chatId);
        if (courierOptional.isEmpty()) {
            Courier newCourier = applicationFacadeService.createNewCourier(chatId);
            courierOptional = Optional.of(newCourier);
        }
        Courier courier = courierOptional.get();
        Optional<Order> activeOrderForCourier = applicationFacadeService.getActiveOrderForCourier(courier);
        if (activeOrderForCourier.isEmpty()) {
            botCommandDTO.setText(CourierBotCommandsPrefixes.AVAILABLE_ORDERS + "_" + 1);
            return availableOrdersCourierBotCommand.process(update, botCommandDTO);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = courierBotKeyboardsFactory.goToActiveOrderKeyboard(activeOrderForCourier.get());
        return new SendMessage(chatId, GO_TO_ACTIVE_ORDER).replyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.DEFAULT;
    }
}
