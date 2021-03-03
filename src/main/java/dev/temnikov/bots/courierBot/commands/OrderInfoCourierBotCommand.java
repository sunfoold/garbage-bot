package dev.temnikov.bots.courierBot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import dev.temnikov.bots.cache.CacheService;
import dev.temnikov.bots.domain.BotCommandDTO;
import dev.temnikov.domain.Order;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderInfoCourierBotCommand extends AbstractCourierBotCommand {
    @Autowired
    CacheService cacheService;

    private static final String ORDER_UNAVAILABLE = "Данный заказ недоступен";

    @Override
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO) {
        long orderId = botUtils.parseId(botCommandDTO);
        long chatId = botCommandDTO.getChatId();
        Optional<Order> unassignedOrderById = cacheService.getUnassignedOrderById(orderId);
        if (unassignedOrderById.isEmpty()) {
            return new SendMessage(chatId, ORDER_UNAVAILABLE).replyMarkup(courierBotKeyboardsFactory.getDefaultCourierKeyBoard());
        }
        File img = botUtils.generateFileFromImage(unassignedOrderById.get().getUserPhotoUrl());
        return new SendPhoto(chatId, img).replyMarkup(courierBotKeyboardsFactory.getAssignOrderKeyBoard(unassignedOrderById.get()));
    }

    @Override
    public String getCommand() {
        return CourierBotCommandsPrefixes.ORDER_INFO;
    }
}
