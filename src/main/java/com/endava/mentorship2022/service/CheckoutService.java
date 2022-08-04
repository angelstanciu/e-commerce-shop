package com.endava.mentorship2022.service;

import com.endava.mentorship2022.model.CartItem;
import com.endava.mentorship2022.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CheckoutService {

    private final OrderService orderService;

    private final CartItemService cartItemService;

    public String placeOrder(User user) {
        List<CartItem> cartItems = cartItemService.findCartItemsByUser(user);
        if (cartItems.isEmpty()) {
            return "Cart is empty.";
        }
        orderService.createOrder(user, cartItems);
        cartItemService.deleteCartByUser(user);

        return "Order has been placed. Thank you for purchase.";
    }

}
