package dev.temnikov.bots.courierBot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import dev.temnikov.bots.courierBot.commands.CourierBotCommandsPrefixes;
import dev.temnikov.domain.Order;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CourierBotKeyboardsFactory {

    public InlineKeyboardMarkup getKeyboardWithAvailableOrders(List<Order> orders, int startPage) {
        if (orders == null || orders.size() == 0) {
            return null;
        }
        int maxPage = calculateMaxPage(orders.size());
        if (startPage > maxPage) {
            startPage = maxPage;
        }
        int lastOrderIndex = calculateStartOrderIndex(startPage, orders.size());
        int numberOfKeyboardLines = calculateNumberOfAvailableAddressesKeyboardLines(startPage, lastOrderIndex);
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[numberOfKeyboardLines][];
        int j = 0;
        for (int i = (startPage - 1) * 5; i <= lastOrderIndex; i++) {
            buttons[j] = new InlineKeyboardButton[1];
            Order order = orders.get(i);
            buttons[j][0] =
                new InlineKeyboardButton(order.getAddress().getStreetBuilding())
                .callbackData(CourierBotCommandsPrefixes.ORDER_INFO + "_" + order.getId());
            j++;
        }
        InlineKeyboardButton[] lastLine;
        if (orders.size() > 5) {
            lastLine = new InlineKeyboardButton[3];
            int leftPage;
            int rightPage;

            if (startPage == 1) {
                leftPage = maxPage;
            } else {
                leftPage = startPage - 1;
            }

            if (startPage >= maxPage) {
                rightPage = 1;
            } else {
                rightPage = startPage + 1;
            }
            lastLine[0] = new InlineKeyboardButton("<< " + leftPage).callbackData(CourierBotCommandsPrefixes.ORDER_INFO + "_" + leftPage);
            lastLine[1] = getCancelKeyBoard();
            lastLine[2] = new InlineKeyboardButton(">> " + rightPage).callbackData(CourierBotCommandsPrefixes.ORDER_INFO + "_" + rightPage);
        } else {
            lastLine = new InlineKeyboardButton[1];
            lastLine[0] = getCancelKeyBoard();
        }
        buttons[buttons.length - 1] = lastLine;
        return new InlineKeyboardMarkup(buttons);
    }

    private InlineKeyboardButton getCancelKeyBoard() {
        return new InlineKeyboardButton("Отмена").callbackData(CourierBotCommandsPrefixes.CANCEL);
    }

    public InlineKeyboardMarkup getDefaultCourierKeyBoard() {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[1];
        buttons[0] = getCancelKeyBoard();
        return new InlineKeyboardMarkup(buttons);
    }

    private int calculateStartOrderIndex(int page, int numberOfOrdersAvailable) {
        return Math.min(numberOfOrdersAvailable - 1, page * 5 - 1);
    }

    int calculateNumberOfAvailableAddressesKeyboardLines(int startPage, int lastOrderIndex) {
        return 1 + (lastOrderIndex + 1 - (startPage - 1) * 5);
    }

    int calculateMaxPage(int numberOfOrders) {
        return (numberOfOrders % 5 == 0) ? numberOfOrders / 5 : numberOfOrders / 5 + 1;
    }

    public Keyboard getAssignOrderKeyBoard(Order order) {
        InlineKeyboardButton[] firstLine = {
            new InlineKeyboardButton("Оформить заказ").callbackData(CourierBotCommandsPrefixes.ASSIGN_ORDER + "_" + order.getId()),
        };
        InlineKeyboardButton[] secondLine = { getCancelKeyBoard() };
        return new InlineKeyboardMarkup(firstLine, secondLine);
    }

    public InlineKeyboardMarkup goToActiveOrderKeyboard(Order order) {
        InlineKeyboardButton[] firstLine = {
            new InlineKeyboardButton("Перейти к активному заказу")
            .callbackData(CourierBotCommandsPrefixes.GO_TO_ACTIVE_ORDER + "_" + order.getId()),
        };
        InlineKeyboardButton[] secondLine = { getCancelKeyBoard() };
        return new InlineKeyboardMarkup(firstLine, secondLine);
    }

    public Keyboard getApproveOrderKeyboard(Order activeOrder) {
        InlineKeyboardButton[] firstLine = {
            new InlineKeyboardButton("Начать выполнение заказа")
            .callbackData(CourierBotCommandsPrefixes.START_ORDER + "_" + activeOrder.getId()),
        };
        InlineKeyboardButton[] secondLine = { getCancelKeyBoard() };
        return new InlineKeyboardMarkup(firstLine, secondLine);
    }
}
