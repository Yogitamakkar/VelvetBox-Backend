package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.domain.OrderStatus;
import com.Ecommerce.demo.domain.PaymentStatus;
import com.Ecommerce.demo.model.*;
import com.Ecommerce.demo.repository.AddressRepository;
import com.Ecommerce.demo.repository.OrderItemRepository;
import com.Ecommerce.demo.repository.OrderRepository;
import com.Ecommerce.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        //we have distinguished the sellers and the corresponding orders by the user
        Map<Long,List<CartItem>> itemsBySeller = cart.getCartItems().stream().collect(
                Collectors.groupingBy(cartItem -> cartItem.getProduct().getSeller().getId())
        );
        Set<Order> orders = new HashSet<>();

        //for each seller we are now setting the order information for each cartItem
        for (Map.Entry<Long,List<CartItem>> entry : itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();
            int totalSellingPrice = items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();
            int totalMrpPrice = items.stream().mapToInt(CartItem::getMrpPrice).sum();
            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrpPrice);
            createdOrder.setTotalSellingPrice(totalSellingPrice);
            createdOrder.setTotalItems(totalItem);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItem cartItem : items){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSize(cartItem.getSize());
                orderItem.setMrpPrice(cartItem.getMrpPrice());
                orderItem.setSellingPrice(cartItem.getSellingPrice());
                orderItem.setUserId(user.getId());

                savedOrder.getOrderItem().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }


        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(()->new Exception("product not found with this id"));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(User user, Long orderId) throws Exception {
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId())){
            throw new Exception("you cant delete this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->new Exception("no order exists with this id"));
    }
}
