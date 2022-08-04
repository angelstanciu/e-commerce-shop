package com.endava.mentorship2022.service;

import com.endava.mentorship2022.model.Order;
import com.endava.mentorship2022.model.OrderDetail;
import com.endava.mentorship2022.repository.OrderDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDetailServiceTest {

    @Mock
    private OrderDetailRepository orderDetailRepository;

    private OrderDetailService orderDetailService;

    @BeforeEach
    void setup() {
        orderDetailService = new OrderDetailService(orderDetailRepository);
    }

    @Test
    void shouldFindAllOrderDetails() {
        // given
        List<OrderDetail> orderDetailList = List.of(
                new OrderDetail(1L,
                        (short) 10,
                        25.0F,
                        250,
                        null,
                        null
                ),
                new OrderDetail(2L,
                        (short) 20,
                        50.0F,
                        1000,
                        null,
                        null
                )
        );
        when(orderDetailRepository.findAll()).thenReturn(orderDetailList);

        //when
        List<OrderDetail> actualOrderDetailList = orderDetailService.findAll();

        //then
        assertThat(orderDetailList).hasSize(2);
        assertThat(orderDetailList).isEqualTo(actualOrderDetailList);

    }

    @Test
    void shouldFindOrderDetailsByOrderId() {
        // given
        Order o1 = new Order();
        o1.setId(5L);

        List<OrderDetail> orderDetailList = List.of(
                new OrderDetail(1L,
                        (short) 10,
                        25.0F,
                        250,
                        o1,
                        null
                ),
                new OrderDetail(2L,
                        (short) 100,
                        30.0F,
                        3000,
                        o1,
                        null
                )
        );

        when(orderDetailRepository.findByOrderId(anyLong())).thenReturn(orderDetailList);

        //when
        List<OrderDetail> actualOrderDetailList = orderDetailService.findByOrderId(anyLong());

        //then
        assertThat(actualOrderDetailList).isEqualTo(orderDetailList);

    }

}
