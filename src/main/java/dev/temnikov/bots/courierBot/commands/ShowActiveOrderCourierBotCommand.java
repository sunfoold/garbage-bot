package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import dev.temnikov.service.CourierService;
import dev.temnikov.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowActiveOrderCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CourierService courierService;

    @Autowired
    OrderService orderService;

    private static final String ERROR = "У вас нет доступа к данному заказу";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        Courier courier = courierService.findByTelegramChatId(chatId).get();
        long orderId = botUtils.parseId(botCommandDTO);
        if (!applicationFacadeService.validateActiveOrder(orderId, courier)) {
            return new SendMessage(chatId, ERROR).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        Order activeOrder = applicationFacadeService.getActiveOrderForCourier(courier).get();
        return new SendMessage(chatId, getResponseString(activeOrder))
        .replyMarkup(courierBotKeyboardsFactory.getApproveOrderKeyboard(activeOrder));
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.GO_TO_ACTIVE_ORDER;
    }

    private String getResponseString(Order order) {
        return String.format("Вам назначен заказ по адресу %s", order.getAddress().getStreetBuilding());
    }
}
