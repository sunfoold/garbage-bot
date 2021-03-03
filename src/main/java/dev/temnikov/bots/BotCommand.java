package dev.temnikov.bots;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import dev.temnikov.bots.domain.BotCommandDTO;

public interface BotCommand {
    public AbstractSendRequest process(Update update, BotCommandDTO botCommandDTO);

    public String getCommand();
}
