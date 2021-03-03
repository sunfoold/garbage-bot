package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Order;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AvailableOrdersCourierBotCommand extends AbstractCourierBotCommand {
    private static final String AVAILABLE_ORDERS_LIST = "Список доступных заказов";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        long chatId = botCommandDTO.getChatId();
        List<Order> allUnassignedOrders = applicationFacadeService.getAllUnassignedOrders();
        int startPage = (int) botUtils.parseId(botCommandDTO);
        InlineKeyboardMarkup keyboardWithAvailableOrders = courierBotKeyboardsFactory.getKeyboardWithAvailableOrders(
            allUnassignedOrders,
            startPage
        );
        return new SendMessage(chatId, AVAILABLE_ORDERS_LIST).replyMarkup(keyboardWithAvailableOrders);
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.AVAILABLE_ORDERS;
    }
}
