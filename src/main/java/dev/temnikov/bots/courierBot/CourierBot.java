package dev.temnikov.bots.courierBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import dev.temnikov.bots.AbstractBot;
import dev.temnikov.bots.BotCommand;
import dev.temnikov.bots.ExpectedCommands;
import dev.temnikov.bots.MessageSender;
import dev.temnikov.bots.clientBot.ClientBotCommand;
import dev.temnikov.bots.courierBot.commands.CourierBotCommand;
import dev.temnikov.bots.courierBot.commands.CourierBotCommandsPrefixes;
import dev.temnikov.bots.domain.BotCommandDTO;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CourierBot extends AbstractBot {
    Map<String, CourierBotCommand> commands;
    private static final TelegramBot bot = new TelegramBot("1530079045:AAEPp_UP6rL9RcOLDNBDatdHOkzMzxcDpoY");

    @Autowired
    MessageSender sender;

    @Autowired
    private CourierBotExpectedCommands courierBotExpectedCommands;

    @Autowired
    CourierBot(List<CourierBotCommand> botCommands) {
        commands = botCommands.stream().collect(Collectors.toMap(CourierBotCommand::getCommand, Function.identity()));
    }

    @PostConstruct
    public void registerBotToSender() {
        sender.registerCourierBot(bot);
    }

    @Scheduled(fixedDelay = 100)
    public void newMessageListener() {
        runListening();
    }

    @Override
    public String getDefaultCommand() {
        return CourierBotCommandsPrefixes.DEFAULT;
    }

    @Override
    public ExpectedCommands getExpectedCommands() {
        return courierBotExpectedCommands;
    }

    @Override
    public BotCommand getCommand(String command) {
        return commands.getOrDefault(command, commands.get(getDefaultCommand()));
    }

    @Override
    public TelegramBot getBot() {
        return bot;
    }

    @Override
    public void sendMessage(AbstractSendRequest sendMessage) {
        sender.addMessageToCourierBot(sendMessage);
    }
}
