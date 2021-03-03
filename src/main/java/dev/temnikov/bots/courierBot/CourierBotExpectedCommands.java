package dev.temnikov.bots.courierBot;

import dev.temnikov.bots.ExpectedCommands;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CourierBotExpectedCommands implements ExpectedCommands {
    private ConcurrentHashMap<Long, String> expectedCommand = new ConcurrentHashMap<>();

    public String getExpectedCommand(Long chatId) {
        return expectedCommand.get(chatId);
    }

    public void addExpectedCommand(Long chatId, String commandPrefix) {
        expectedCommand.put(chatId, commandPrefix);
    }

    public void deleteExpectedCommand(Long chatId) {
        expectedCommand.remove(chatId);
    }
}
