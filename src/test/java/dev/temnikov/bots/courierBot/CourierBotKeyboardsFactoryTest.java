package dev.temnikov.bots.courierBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

class CourierBotKeyboardsFactoryTest {
    private List<Order> orders;
    private TelegramBot courierBot;
    private long testCourierChatId;
    CourierBotKeyboardsFactory courierBotKeyboardsFactory;

    @BeforeClass
    private void init() {
        orders = generateOrders(15);
        courierBot = new TelegramBot("1530079045:AAEPp_UP6rL9RcOLDNBDatdHOkzMzxcDpoY");
        testCourierChatId = 471944193L;
        courierBotKeyboardsFactory = new CourierBotKeyboardsFactory();
    }

    @Test
    void getKeyboardWithAvailableOrdersPage1() {
        init();
        InlineKeyboardMarkup keyboardWithAvailableOrders = courierBotKeyboardsFactory.getKeyboardWithAvailableOrders(orders, 1);
        courierBot.execute(new SendMessage(testCourierChatId, "Тест первой страницы адресов").replyMarkup(keyboardWithAvailableOrders));
    }

    @Test
    void getKeyboardWithAvailableOrdersPage2() {
        init();
        InlineKeyboardMarkup keyboardWithAvailableOrders = courierBotKeyboardsFactory.getKeyboardWithAvailableOrders(orders, 2);
        courierBot.execute(new SendMessage(testCourierChatId, "Тест второй страницы адресов").replyMarkup(keyboardWithAvailableOrders));
    }

    @Test
    void getKeyboardWithAvailableOrdersPage3() {
        init();
        InlineKeyboardMarkup keyboardWithAvailableOrders = courierBotKeyboardsFactory.getKeyboardWithAvailableOrders(orders, 3);
        courierBot.execute(new SendMessage(testCourierChatId, "Тест 3 страницы адресов").replyMarkup(keyboardWithAvailableOrders));
    }

    @Test
    void getKeyboardWithAvailableOrdersPage4() {
        init();
        InlineKeyboardMarkup keyboardWithAvailableOrders = courierBotKeyboardsFactory.getKeyboardWithAvailableOrders(orders, 4);
        courierBot.execute(new SendMessage(testCourierChatId, "Тест несуществующей").replyMarkup(keyboardWithAvailableOrders));
    }

    @Test
    void getKeyboardWith11AvailableOrdersPage3() {
        init();
        orders = generateOrders(11);
        InlineKeyboardMarkup keyboardWithAvailableOrders = courierBotKeyboardsFactory.getKeyboardWithAvailableOrders(orders, 4);
        courierBot.execute(
            new SendMessage(testCourierChatId, "Тест неполностью заполненной страницы").replyMarkup(keyboardWithAvailableOrders)
        );
    }

    private List<Order> generateOrders(int size) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            orders.add(generateOrder("адрес " + i + 1));
        }
        return orders;
    }

    private Order generateOrder(String address) {
        Order order = new Order();
        order.setId(new Random().nextLong());
        Address address1 = new Address();
        address1.setStreetBuilding(address);
        order.setAddress(address1);
        return order;
    }
}
