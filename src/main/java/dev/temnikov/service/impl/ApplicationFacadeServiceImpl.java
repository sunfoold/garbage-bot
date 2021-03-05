package dev.temnikov.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import dev.temnikov.bots.MessageSender;
import dev.temnikov.bots.cache.CacheService;
import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import dev.temnikov.domain.enumeration.OrderStatus;
import dev.temnikov.service.*;
import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFacadeServiceImpl implements ApplicationFacadeService {
    @Autowired
    private AddressService addressService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    OrderService orderService;

    @Autowired
    MessageSender messageSender;

    @Autowired
    CourierService courierService;

    @Override
    public Order createOrder(Long telegramChatId, Long addressId) {
        Optional<Address> one = addressService.findOne(addressId);
        Optional<AppUser> user = appUserService.findByTelegramChatId(telegramChatId);
        Order order = new Order();
        order.setOrderDate(Instant.now());
        order.setOrderStatus(OrderStatus.NEW);
        order.setAddress(one.get());
        order.setUser(user.get());
        Order save = orderService.save(order);
        cacheService.putOrderWithoutUploadedPhoto(telegramChatId, save);
        return save;
    }

    @Override
    public Optional<Order> getOrderWithoutUploadedPhoto(Long telegramChatId) {
        return cacheService.getOrderWithoutUploadedPhoto(telegramChatId);
    }

    @Override
    public Order addUserPhotoToOrder(Order order, String fileUrl, Long telegramChatId) {
        order.setUserPhotoUrl(fileUrl);
        order = orderService.save(order);
        putOrderAvailableForAssigning(order);
        cacheService.deleteOrderWithoutUploadedPhoto(telegramChatId);
        return order;
    }

    @Override
    public AppUser createNewUser(Long chatId) {
        AppUser appUser = new AppUser();
        appUser.telegramChatId(chatId);
        return appUserService.save(appUser);
    }

    @Override
    public void putOrderAvailableForAssigning(Order order) {
        cacheService.putOrderAvailableForAssigning(order);
    }

    @Override
    public Order getUnassignedOrderByIdAndDelete(Long orderId) {
        return cacheService.getUnassignedOrderByIdAndDelete(orderId);
    }

    @Override
    public Order assignOrderToCourier(Order order, Courier courier) {
        order = orderService.assignOrderToCourier(order, courier);
        cacheService.deleteFromAwaitingQueue(order.getId());
        cacheService.addActiveOrderForCourier(courier, order);
        messageSender.addMessageToClientBot(new SendMessage(order.getUser().getTelegramChatId(), "На ваш заказ назначен курьер"));
        return order;
    }

    @Override
    public Optional<Order> getActiveOrderForCourier(Courier courier) {
        return cacheService.getActiveOrderForCourier(courier.getId());
    }

    @Override
    public List<Order> getAllUnassignedOrders() {
        return cacheService.getAllUnassignedOrders();
    }

    @Override
    public Courier createNewCourier(Long chatId) {
        Courier courier = new Courier();
        courier.setTelegramChatId(chatId);
        return courierService.save(courier);
    }

    @Override
    public boolean validateActiveOrder(long orderId, Courier courier) {
        Optional<Order> activeOrderForCourier = cacheService.getActiveOrderForCourier(courier.getId());
        return activeOrderForCourier.isPresent() && activeOrderForCourier.get().getId().equals(orderId);
    }

    @Override
    public boolean mayCourierStartOrder(Optional<Order> activeOrderOpt, long orderId, Courier courier) {
        if (activeOrderOpt.isEmpty()) {
            return false;
        }
        if (courier == null) {
            return false;
        }
        if (orderId < 1) {
            return false;
        }
        Order order = activeOrderOpt.get();
        if (order.getId() != orderId) {
            return false;
        }
        return order.getCourier().getId().equals(courier.getId());
    }

    @Override
    public Order startOrderByCourier(long orderId, Courier courier, String fileUrl, File file) {
        Optional<Order> activeOrderForCourier = cacheService.getActiveOrderForCourier(courier.getId());
        Order order = activeOrderForCourier.get();
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setOrderStartDate(Instant.now());
        order.setCourierPhotoUrl(fileUrl);
        order = orderService.save(order);
        cacheService.addActiveOrderForCourier(courier, order);
        messageSender.addMessageToClientBot(new SendPhoto(order.getUser().getTelegramChatId(), file));
        messageSender.addMessageToClientBot(new SendMessage(order.getUser().getTelegramChatId(), "Курьер начал выполнение вашего заказа"));
        return order;
    }

    @Override
    public Order completeOrderByCourier(long orderId, Courier courier, String fileUrl, File file) {
        Optional<Order> activeOrderForCourier = cacheService.getActiveOrderForCourier(courier.getId());
        Order order = activeOrderForCourier.get();
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderFinishDate(Instant.now());
        order.setEndOrderPhotoUrl(fileUrl);
        order = orderService.save(order);
        cacheService.deleteActiveOrderForCourier(courier);
        messageSender.addMessageToClientBot(new SendPhoto(order.getUser().getTelegramChatId(), file));
        messageSender.addMessageToClientBot(
            new SendMessage(order.getUser().getTelegramChatId(), "Курьер завершил выполнение вашего заказа")
        );
        return order;
    }

    @Override
    public boolean checkUserAndDeleteAddress(AppUser appUser, Address address) {
        Set<Address> userAddresses = appUser.getAddresses();
        if (!userAddresses.contains(address)) {
            return false;
        }
        //        userAddresses.remove(address);
        //        appUser.setAddresses(userAddresses);
        addressService.delete(address.getId());
        //        appUserService.save(appUser);
        return true;
    }

    @Override
    public boolean checkUserAddress(AppUser appUser, long addressId) {
        Optional<Address> one = addressService.findOne(addressId);
        if (one.isEmpty()) {
            return false;
        }
        Address address = one.get();
        Set<Address> userAddresses = appUser.getAddresses();
        if (!userAddresses.contains(address)) {
            return false;
        }
        return true;
    }
}
