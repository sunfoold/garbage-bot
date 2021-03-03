package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.courierBot.CourierBotExpectedCommands;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Courier;
import dev.temnikov.service.CourierService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartOrderCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CourierService courierService;

    @Autowired
    CourierBotExpectedCommands courierBotExpectedCommands;

    private static final String SEND_PHOTO_TO_START =
        "Для начала выполнения заказа сфотографируйте мусор у входной двери заказчика и пришлите фото в обратном сообщении";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        long chatId = botCommandDTO.getChatId();
        long orderId = botUtils.parseId(botCommandDTO);
        Optional<Courier> courierOptional = courierService.findByTelegramChatId(chatId);
        if (!applicationFacadeService.validateActiveOrder(orderId, courierOptional.get())) {
            return botUtils.getNoAccessMessage(chatId, courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        courierBotExpectedCommands.addExpectedCommand(chatId, CourierBotCommandsPrefixes.START_ORDER_PHOTO_UPLOAD + "_" + orderId);
        return new SendMessage(chatId, SEND_PHOTO_TO_START).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.START_ORDER;
    }
}
