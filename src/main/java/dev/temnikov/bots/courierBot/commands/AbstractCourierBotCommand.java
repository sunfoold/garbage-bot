package dev.temnikov.bots.courierBot.commands;

import dev.temnikov.bots.BotUtils;
import dev.temnikov.bots.courierBot.CourierBotKeyboardsFactory;
import dev.temnikov.service.ApplicationFacadeService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCourierBotCommand implements CourierBotCommand {
    @Autowired
    CourierBotKeyboardsFactory courierBotKeyboardsFactory;

    @Autowired
    ApplicationFacadeService applicationFacadeService;

    @Autowired
    BotUtils botUtils;
}
