package dev.temnikov.bots.clientBot.commands;

import dev.temnikov.bots.BotUtils;
import dev.temnikov.bots.clientBot.ClientBotCommand;
import dev.temnikov.bots.clientBot.ClientBotExpectedCommands;
import dev.temnikov.bots.clientBot.ClientBotKeyboardsFactory;
import dev.temnikov.service.ApplicationFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractClientBotCommand implements ClientBotCommand {
    @Autowired
    ClientBotExpectedCommands clientBotExpectedCommands;

    @Autowired
    ApplicationFacadeService facadeService;

    @Autowired
    ClientBotKeyboardsFactory clientBotKeyboardsFactory;

    @Autowired
    BotUtils botUtils;
}
