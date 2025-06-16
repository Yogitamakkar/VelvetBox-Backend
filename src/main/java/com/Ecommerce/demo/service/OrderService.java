package com.Ecommerce.demo.service;

import com.Ecommerce.demo.domain.OrderStatus;
import com.Ecommerce.demo.model.*;


import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long orderId) throws Exception;
    List<Order> userOrderHistory(Long userId);
    List<Order> sellerOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder(User user,Long orderId) throws Exception;
    OrderItem getOrderItemById(Long id) throws Exception;

}
