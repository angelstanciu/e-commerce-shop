package com.endava.mentorship2022.service;

import com.endava.mentorship2022.model.OrderDetail;
import com.endava.mentorship2022.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailsRepository;

    public List<OrderDetail> findAll() {
        return orderDetailsRepository.findAll();
    }

    public List<OrderDetail> findByOrderId(long orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }

}
