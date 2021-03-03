package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.courierBot.CourierBotExpectedCommands;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import dev.temnikov.service.CourierService;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompleteOrderCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CourierService courierService;

    @Autowired
    CourierBotExpectedCommands courierBotExpectedCommands;

    private static final String SEND_PHOTO_TO_COMPLETE =
        "Вы начали выполнение заказа. Для завершения выполнения заказа сфотографируйте мусор в мусорном контейнере и пришлите фото в обратном сообщении";
    private static final String ORDER_COMPLETED = "Вы завершили выполнение заказа";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        long chatId = botCommandDTO.getChatId();
        long orderId = botUtils.parseId(botCommandDTO);
        Courier courier = courierService.findByTelegramChatId(chatId).get();
        if (botCommandDTO.getFileUrl() == null) {
            return new SendMessage(chatId, SEND_PHOTO_TO_COMPLETE);
        }
        if (!applicationFacadeService.validateActiveOrder(orderId, courier)) {
            return botUtils.getNoAccessMessage(chatId, courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        File file = botUtils.generateFileFromImage(botCommandDTO.getFileUrl());
        Order order = applicationFacadeService.completeOrderByCourier(orderId, courier, botCommandDTO.getFileUrl(), file);
        courierBotExpectedCommands.deleteExpectedCommand(chatId);
        return new SendMessage(chatId, ORDER_COMPLETED).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.COMPLETE_ORDER;
    }
}
