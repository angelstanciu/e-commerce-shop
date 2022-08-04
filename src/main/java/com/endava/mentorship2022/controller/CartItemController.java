package com.endava.mentorship2022.controller;

import com.endava.mentorship2022.model.CartItem;
import com.endava.mentorship2022.model.User;
import com.endava.mentorship2022.service.CartItemService;
import com.endava.mentorship2022.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    private final UserService userService;

    @GetMapping()
    public List<CartItem> viewCart(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return cartItemService.findCartItemsByUser(user);
    }

    @PostMapping("/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable long productId, @PathVariable short quantity,
                                   Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return cartItemService.addProductToCart(user, productId, quantity);
    }

    @PutMapping("/update/{productId}/{quantity}")
    public String updateProductQuantity(@PathVariable long productId, @PathVariable short quantity,
                                        Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return cartItemService.updateProductQuantity(user, productId, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable long productId,
                                        Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return cartItemService.removeProductFromCart(user, productId);
    }

    @DeleteMapping()
    public String deleteCart(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return cartItemService.deleteCartByUser(user);
    }

}