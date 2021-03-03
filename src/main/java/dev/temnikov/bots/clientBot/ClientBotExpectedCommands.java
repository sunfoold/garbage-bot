package dev.temnikov.bots.clientBot;

import dev.temnikov.bots.ExpectedCommands;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ClientBotExpectedCommands implements ExpectedCommands {
    private HashMap<Long, String> expectedCommand = new HashMap<>();

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
