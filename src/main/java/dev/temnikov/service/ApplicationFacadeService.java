package dev.temnikov.service;

import dev.temnikov.domain.Address;
import dev.temnikov.domain.AppUser;
import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ApplicationFacadeService {
    public Order createOrder(Long telegramChatId, Long addressId);

    public Optional<Order> getOrderWithoutUploadedPhoto(Long telegramChatId);

    public Order addUserPhotoToOrder(Order order, String fileUrl, Long telegramChatId);

    public AppUser createNewUser(Long chatId);

    public void putOrderAvailableForAssigning(Order order);

    public Order getUnassignedOrderByIdAndDelete(Long orderId);

    public Order assignOrderToCourier(Order order, Courier courier);

    public Optional<Order> getActiveOrderForCourier(Courier courier);

    List<Order> getAllUnassignedOrders();

    public Courier createNewCourier(Long chatId);

    boolean validateActiveOrder(long orderId, Courier courier);

    boolean mayCourierStartOrder(Optional<Order> activeOrderOpt, long orderId, Courier courier);

    Order startOrderByCourier(long orderId, Courier courier, String fileUrl, File file);

    Order completeOrderByCourier(long orderId, Courier courier, String fileUrl, File file);

    boolean checkUserAndDeleteAddress(AppUser appUser, Address addressId);

    boolean checkUserAddress(AppUser appUser, long addressId);
}
