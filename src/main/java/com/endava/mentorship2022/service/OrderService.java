
package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.OrderNotFound;
import com.endava.mentorship2022.model.CartItem;
import com.endava.mentorship2022.model.Order;
import com.endava.mentorship2022.model.OrderDetail;
import com.endava.mentorship2022.model.OrderStatus;
import com.endava.mentorship2022.model.Product;
import com.endava.mentorship2022.model.User;
import com.endava.mentorship2022.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findOrdersByUserId(Long userId) {
        User user = userService.findById(userId);
        return orderRepository.findByUser(user);
    }

    public Order findById(long id) {
        return orderRepository.findById(id).
                orElseThrow(() -> new OrderNotFound("Order: " + id + " not found."));
    }

    public Order save(Order order) {
        // Make a copy of orderDetails
        Set<OrderDetail> orderDetails = new HashSet<>(order.getOrderDetails());
        order.setOrderDetails(null);

        // Save without orderDetails first, in order to get an orderId to use in orderDetails
        orderRepository.save(order);

        // Add orderDetails and then save order
        orderDetails.forEach(orderDetail -> orderDetail.setOrder(order));
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public void deleteById(Long id) {
        findById(id);
        orderRepository.deleteById(id);
    }

    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = findById(id);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Order createOrder(User user, List<CartItem> cartItems) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setTotal(calculateTotal(cartItems));
        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setSubtotal(cartItem.getSubtotal());

            orderDetails.add(orderDetail);
        }

        return orderRepository.save(newOrder);
    }

    private float calculateTotal(List<CartItem> cartItems) {
        float total = 0.0F;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getSubtotal();
        }

        return total;
    }

}