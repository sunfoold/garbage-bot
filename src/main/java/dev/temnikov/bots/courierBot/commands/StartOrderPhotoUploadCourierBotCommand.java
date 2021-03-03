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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartOrderPhotoUploadCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CourierService courierService;

    @Autowired
    CourierBotExpectedCommands courierBotExpectedCommands;

    private static final String SEND_PHOTO_TO_START =
        "Для начала выполнения заказа сфотографируйте мусор у входной двери заказчика и пришлите фото в обратном сообщении";
    private static final String SEND_PHOTO_TO_COMPLETE =
        "Вы начали выполнение заказа. Для завершения выполнения заказа сфотографируйте мусор в мусорном контейнере и пришлите фото в обратном сообщении";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        long chatId = botCommandDTO.getChatId();
        long orderId = botUtils.parseId(botCommandDTO);
        Courier courier = courierService.findByTelegramChatId(chatId).get();
        if (botCommandDTO.getFileUrl() == null) {
            return new SendMessage(chatId, SEND_PHOTO_TO_START).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        if (!applicationFacadeService.validateActiveOrder(orderId, courier)) {
            return botUtils.getNoAccessMessage(chatId, courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        Optional<Order> activeOrderOpt = applicationFacadeService.getActiveOrderForCourier(courier);
        if (!applicationFacadeService.mayCourierStartOrder(activeOrderOpt, orderId, courier)) {
            return botUtils.getWrongOrderStatus(chatId, courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        File file = botUtils.generateFileFromImage(botCommandDTO.getFileUrl());
        Order order = applicationFacadeService.startOrderByCourier(orderId, courier, botCommandDTO.getFileUrl(), file);
        courierBotExpectedCommands.deleteExpectedCommand(chatId);
        courierBotExpectedCommands.addExpectedCommand(chatId, CourierBotCommandsPrefixes.COMPLETE_ORDER + "_" + order.getId());
        return new SendMessage(chatId, SEND_PHOTO_TO_COMPLETE);
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.START_ORDER_PHOTO_UPLOAD;
    }
}
