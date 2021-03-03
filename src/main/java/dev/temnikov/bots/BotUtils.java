package dev.temnikov.bots;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.domain.BotCommandDTO;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotUtils {
    @Autowired
    private static final String NO_ACCESS = "У вас нет доступа к данному заказу";

    private static final String CANT_START = "Невозможно изменить статус заказа";

    public File generateFileFromImage(String photoUrl) {
        File file = null;
        try {
            URL url = new URL(photoUrl);
            file = new File("image.jpg");
            FileUtils.copyURLToFile(url, file);
        } catch (Exception e) {
            System.out.println(e);
        }
        return file;
    }

    public long parseId(BotCommandDTO commandDTO) {
        String text = commandDTO.getText();
        String[] command = parseCommand(text);
        try {
            return Long.parseLong(command[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    public SendMessage getNoAccessMessage(Long chatId, Keyboard keyboard) {
        SendMessage res = new SendMessage(chatId, NO_ACCESS);
        if (keyboard != null) {
            res = res.replyMarkup(keyboard);
        }
        return res;
    }

    public SendMessage getWrongOrderStatus(Long chatId, Keyboard keyboard) {
        SendMessage res = new SendMessage(chatId, CANT_START);
        if (keyboard != null) {
            res = res.replyMarkup(keyboard);
        }
        return res;
    }

    private String[] parseCommand(String command) {
        return command.split("_");
    }
}
