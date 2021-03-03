package dev.temnikov.bots.clientBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import dev.temnikov.bots.AbstractBot;
import dev.temnikov.bots.BotCommand;
import dev.temnikov.bots.ExpectedCommands;
import dev.temnikov.bots.MessageSender;
import dev.temnikov.bots.clientBot.commands.ClientBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ClientBot extends AbstractBot {
    private static final TelegramBot bot = new TelegramBot("1602107973:AAFJep5oNdy1TEZypuq7DPZJwHtEyl5ela0");
    Map<String, ClientBotCommand> commands;

    @Autowired
    ClientBotExpectedCommands clientBotExpectedCommands;

    @Autowired
    MessageSender sender;

    @Autowired
    ClientBot(List<ClientBotCommand> botCommands) {
        commands = botCommands.stream().collect(Collectors.toMap(ClientBotCommand::getCommand, Function.identity()));
    }

    @PostConstruct
    public void registerBotToSender() {
        sender.registerClientBot(bot);
    }

    @Scheduled(fixedDelay = 100)
    public void newMessageListener() {
        runListening();
    }

    @Override
    public String getDefaultCommand() {
        return ClientBotCommandsPrefixes.START;
    }

    @Override
    public ExpectedCommands getExpectedCommands() {
        return clientBotExpectedCommands;
    }

    @Override
    public BotCommand getCommand(String command) {
        return commands.get(command);
    }

    @Override
    public TelegramBot getBot() {
        return bot;
    }

    @Override
    public void sendMessage(AbstractSendRequest sendMessage) {
        sender.addMessageToClientBot(sendMessage);
    }
}
