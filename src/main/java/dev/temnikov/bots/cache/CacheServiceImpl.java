package dev.temnikov.bots.cache;

import dev.temnikov.domain.Courier;
import dev.temnikov.domain.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class CacheServiceImpl implements CacheService {
    private ConcurrentHashMap<Long, Order> ordersWithoutPhoto = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Long, Order> notAssignedOrders = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Long, Order> activeOrdersForCourier = new ConcurrentHashMap<>();

    @Override
    public Optional<Order> getOrderWithoutUploadedPhoto(Long chatId) {
        return Optional.ofNullable(ordersWithoutPhoto.get(chatId));
    }

    @Override
    public void putOrderWithoutUploadedPhoto(Long chatId, Order order) {
        ordersWithoutPhoto.put(chatId, order);
    }

    @Override
    public void deleteOrderWithoutUploadedPhoto(Long chatId) {
        ordersWithoutPhoto.remove(chatId);
    }

    @Override
    public void putOrderAvailableForAssigning(Order order) {
        notAssignedOrders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> deleteFromAwaitingQueue(Long orderId) {
        return Optional.ofNullable(notAssignedOrders.remove(orderId));
    }

    @Override
    public Optional<Order> getUnassignedOrderById(Long orderId) {
        return Optional.ofNullable(notAssignedOrders.get(orderId));
    }

    @Override
    public List<Order> getAllUnassignedOrders() {
        return new ArrayList<>(notAssignedOrders.values());
    }

    @Override
    public Order getUnassignedOrderByIdAndDelete(long orderId) {
        Order order = notAssignedOrders.get(orderId);
        if (order != null) {
            notAssignedOrders.remove(orderId);
        }
        return order;
    }

    @Override
    public Optional<Order> getActiveOrderForCourier(long courierId) {
        return Optional.ofNullable(activeOrdersForCourier.get(courierId));
    }

    @Override
    public void addActiveOrderForCourier(Courier courier, Order order) {
        activeOrdersForCourier.put(courier.getId(), order);
    }

    @Override
    public void deleteActiveOrderForCourier(Courier courier) {
        activeOrdersForCourier.remove(courier.getId());
    }
}
