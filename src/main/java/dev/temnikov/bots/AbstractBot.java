package dev.temnikov.bots;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import dev.temnikov.bots.domain.BotCommandDTO;
import java.util.List;

public abstract class AbstractBot implements BotInterface {
    private int offset;

    protected List<Update> executeForUpdates(TelegramBot bot) {
        GetUpdates getUpdates = new GetUpdates().limit(1).offset(offset);
        GetUpdatesResponse execute = bot.execute(getUpdates);
        return execute.updates();
    }

    protected BotCommand parseBotCommand(TelegramBot bot, BotCommandDTO botCommandDTO) {
        if (botCommandDTO.getFileId() != null) {
            getFileUrl(bot, botCommandDTO);
        }
        String text = botCommandDTO.getText();
        BotCommand command = null;
        String expectedCommand = getExpectedCommands().getExpectedCommand(botCommandDTO.getMessage().chat().id());
        if (text != null) {
            String[] s = botCommandDTO.getText().split("_");
            String translated = translateText(s[0]);
            command = getCommand(translated);
        }
        if (command == null) {
            if (expectedCommand != null) {
                String[] s = expectedCommand.split("_");
                String newCommand =s[0];
                command = getCommand(s[0]);
                if (s.length<2){
                    newCommand = newCommand +"_" + text;
                }
                botCommandDTO.setText(newCommand);
            } else {
                command = getCommand(getDefaultCommand());
            }
        }
        return command;
    }

    protected abstract String translateText(String s);

    protected void runListening() {
        while (true) {
            try {
                List<Update> updates = executeForUpdates(getBot());
                if (updates != null && !updates.isEmpty()) {
                    Update update = updates.get(0);
                    try {
                        BotCommandDTO botCommandDTO = getMessageText(update);
                        if (botCommandDTO.getMessage() != null) {
                            BotCommand command = parseBotCommand(getBot(), botCommandDTO);
                            AbstractSendRequest messageToSend = command.process(update, botCommandDTO);
                            if (messageToSend != null) {
                                sendMessage(messageToSend);
                            }
                        }
                        offset = update.updateId() + 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        offset = update.updateId() + 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void getFileUrl(TelegramBot bot, BotCommandDTO botCommandDTO) {
        GetFile request = new GetFile(botCommandDTO.getFileId());
        GetFileResponse getFileResponse = bot.execute(request);
        File file = getFileResponse.file();
        String fullFilePath = bot.getFullFilePath(file);
        botCommandDTO.setFileUrl(fullFilePath);
    }

    protected BotCommandDTO getMessageText(Update update) {
        Message message = update.message();
        String text = null;
        String fileId = null;
        if (message != null) {
            text = message.text();
            PhotoSize[] photo = message.photo();
            if (photo != null && photo.length > 0) {
                fileId = photo[photo.length - 1].fileId();
            }
        } else if (update.callbackQuery() != null) {
            message = update.callbackQuery().message();
            text = update.callbackQuery().data();
        }
        BotCommandDTO botCommandDTO = new BotCommandDTO();
        botCommandDTO.setChatId(message.chat().id());
        botCommandDTO.setText(text);
        botCommandDTO.setMessage(message);
        botCommandDTO.setFileId(fileId);
        return botCommandDTO;
    }
}
