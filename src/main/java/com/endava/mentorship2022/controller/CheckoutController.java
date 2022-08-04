package com.endava.mentorship2022.controller;

import com.endava.mentorship2022.model.User;
import com.endava.mentorship2022.service.CheckoutService;
import com.endava.mentorship2022.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    private final UserService userService;

    @PostMapping()
    public String placeOrder(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return checkoutService.placeOrder(user);
    }

}
