package com.endava.mentorship2022.controller;

import com.endava.mentorship2022.model.OrderDetail;
import com.endava.mentorship2022.service.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-details")
@AllArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailsService;

    @GetMapping
    public List<OrderDetail> findAll() {
        return orderDetailsService.findAll();
    }

    @GetMapping("/{id}")
    public List<OrderDetail> findByOrderId(@PathVariable long id) {
        return orderDetailsService.findByOrderId(id);
    }

}
