package dev.temnikov.bots.cache;

import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import java.util.List;
import java.util.Optional;

public interface CacheService {
    public Optional<Order> getOrderWithoutUploadedPhoto(Long chatId);

    public void putOrderWithoutUploadedPhoto(Long chatId, Order order);

    public void deleteOrderWithoutUploadedPhoto(Long chatId);

    public void putOrderAvailableForAssigning(Order order);

    public Optional<Order> deleteFromAwaitingQueue(Long orderId);

    public Optional<Order> getUnassignedOrderById(Long orderId);

    public List<Order> getAllUnassignedOrders();

    Order getUnassignedOrderByIdAndDelete(long orderId);

    Optional<Order> getActiveOrderForCourier(long CourierId);

    void addActiveOrderForCourier(Courier courier, Order order);

    void deleteActiveOrderForCourier(Courier courier);
}
