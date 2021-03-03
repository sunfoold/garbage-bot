package dev.temnikov.bots;

public interface ExpectedCommands {
    public String getExpectedCommand(Long chatId);

    public void addExpectedCommand(Long chatId, String commandPrefix);

    public void deleteExpectedCommand(Long chatId);
}
