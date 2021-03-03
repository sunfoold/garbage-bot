package dev.temnikov.bots.clientBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.cache.CacheService;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Order;
import dev.temnikov.service.OrderService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UploadPhotoClientBotCommand extends AbstractClientBotCommand {
    private static final String SEND_PHOTO = "Пришлите фото, чтобы завершить подтверждение заказа";
    private static final String CANT_FIND_ORDER = "Не можем найти активный заказ";
    private static final String ORDER_CREATED = "Заказ принят! Ожидайте назначения курьера";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        Long chatId = botCommandDTO.getChatId();
        String fileUrl = botCommandDTO.getFileUrl();
        if (fileUrl == null) {
            return new SendMessage(chatId, SEND_PHOTO);
        }
        Optional<Order> orderWithoutUploadedPhoto = facadeService.getOrderWithoutUploadedPhoto(chatId);
        if (orderWithoutUploadedPhoto.isEmpty()) {
            clientBotExpectedCommands.deleteExpectedCommand(chatId);
            return new SendMessage(chatId, CANT_FIND_ORDER);
        }
        facadeService.addUserPhotoToOrder(orderWithoutUploadedPhoto.get(), fileUrl, chatId);
        clientBotExpectedCommands.deleteExpectedCommand(chatId);
        return new SendMessage(chatId, ORDER_CREATED);
    }

    @Override
    public String getCommand() {
        return ClientBotCommandsPrefixes.UPLOAD_PHOTO;
    }
}
