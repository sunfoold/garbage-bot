package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.MessageSender;
import dev.temnikov.bots.cache.CacheService;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import dev.temnikov.domain.enumeration.OrderStatus;
import dev.temnikov.service.CourierService;
import dev.temnikov.service.OrderService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignOrderCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CourierService courierService;

    @Autowired
    OrderService orderService;

    @Autowired
    MessageSender messageSender;

    private static final String ORDER_UNAVAILABLE = "Данный заказ недоступен";
    private static final String ERROR = "Произошла ошибка";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        long chatId = botCommandDTO.getChatId();
        long orderId = botUtils.parseId(botCommandDTO);
        Order order = applicationFacadeService.getUnassignedOrderByIdAndDelete(orderId);
        if (order == null) {
            return new SendMessage(chatId, ORDER_UNAVAILABLE).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        Optional<Courier> courierOpt = courierService.findByTelegramChatId(chatId);
        if (courierOpt.isEmpty()) {
            applicationFacadeService.putOrderAvailableForAssigning(order);
            return new SendMessage(chatId, ERROR).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        applicationFacadeService.assignOrderToCourier(order, courierOpt.get());
        InlineKeyboardMarkup inlineKeyboardMarkup = courierBotKeyboardsFactory.goToActiveOrderKeyboard(order);
        return new SendMessage(chatId, "Заказ по адресу " + order.getAddress().getStreetBuilding() + " назначен на вас")
        .replyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.ASSIGN_ORDER;
    }
}
