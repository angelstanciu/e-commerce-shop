package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.OrderNotFound;
import com.endava.mentorship2022.model.*;
import com.endava.mentorship2022.repository.OrderRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.endava.mentorship2022.model.OrderStatus.PENDING;
import static com.endava.mentorship2022.model.UserStatus.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, userService);
    }

    @Test
    @DisplayName("Find all Orders")
    void findAllOrdersTest() {
        // given
        List<Order> orders = List.of(
                new Order(
                        1L,
                        LocalDate.of(2022, 1, 1),
                        300,
                        null,
                        null,
                        PENDING),
                new Order(
                        2L,
                        LocalDate.of(2022, 1, 1),
                        900,
                        null,
                        null,
                        PENDING)
        );
        given(orderRepository.findAll()).willReturn(orders);

        // when
        List<Order> actualOrders = orderService.findAll();

        // then
        verify(orderRepository).findAll();
        AssertionsForClassTypes.assertThat(actualOrders).isEqualTo(orders);
    }

    @Test
    void findOrderByIdTest() {
        // given
        Order orderToBeFound = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                300,
                null,
                null,
                PENDING
        );
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(orderToBeFound));

        // when
        Order actualOrder = orderService.findById(1);

        // then
        verify(orderRepository).findById(anyLong());
        AssertionsForClassTypes.assertThat(actualOrder).isEqualTo(orderToBeFound);
    }

    @Test
    @DisplayName("Should throw OrderNotFound Exception")
    void findOrderByIdExceptionTest() {
        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderService.findById(anyLong()))
                .isInstanceOf(OrderNotFound.class)
                .hasMessageContaining("Order: " + 0 + " not found.");
    }

    @Test
    void saveOrderTest() {
        // given
        Order order1 = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                300,
                null,
                Set.of(
                        new OrderDetail(1L,(short) 2, 5.5F, 11F, null, null)
                ),
                PENDING
        );
        given(orderRepository.save(order1)).willReturn(order1);

        // when
        Order actualOrder = orderService.save(order1);

        // then
        verify(orderRepository, times(2)).save(any());
        AssertionsForClassTypes.assertThat(actualOrder).isEqualTo(order1);
    }


    @Test
    void deleteOrderByIdTest() {
        // given
        Order orderToDelete = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                300,
                null,
                null,
                PENDING
        );
        given(orderRepository.findById(1L)).willReturn(Optional.of(orderToDelete));

        // when
        orderService.deleteById(1L);

        // then
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrderExceptionTest() {
        // given
        Order orderToDelete = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                300,
                null,
                null,
                PENDING
        );
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.deleteById(1L))
                .isInstanceOf(OrderNotFound.class)
                .hasMessageContaining("Order: " + 1L + " not found.");

        // then
        verify(orderRepository, never()).delete(orderToDelete);
    }

    @Test
    void findAllOrdersByUserTest() {
        // given
        List<Order> orderList = List.of(
                new Order(
                        1L,
                        LocalDate.of(2022, 1, 1),
                        300,
                        null,
                        null,
                        PENDING
                ),
                new Order(
                        2L,
                        LocalDate.of(2022, 1, 1),
                        900,
                        null,
                        null,
                        PENDING)
        );

        User user = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                ACTIVE,
                Set.of(new Role("ADMIN"))
        );
        given(userService.findById(anyLong())).willReturn(user);
        given(orderRepository.findByUser(any(User.class))).willReturn(orderList);

        // when
        List actualOrders = orderService.findOrdersByUserId(anyLong());

        // then
        AssertionsForClassTypes.assertThat(actualOrders).isEqualTo(orderList);
    }

    @Test
    void updateOrderStatusTest() {
        // given
        Order orderToUpdate = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                300,
                null,
                null,
                PENDING
        );
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(orderToUpdate));
        given(orderRepository.save(orderToUpdate)).willReturn(orderToUpdate);

        // when
        orderService.updateStatus(1L, OrderStatus.COMPLETED);

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(orderArgumentCaptor.capture());

        Order capturedOrder = orderArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(capturedOrder).isEqualTo(orderToUpdate);
    }

    @Test
    void willThrowOrderNotFoundForUpdateStatusTest() {
        // given
        Order order = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                300,
                null,
                null,
                PENDING
        );
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.updateStatus(1L, OrderStatus.COMPLETED))
                .isInstanceOf(OrderNotFound.class)
                .hasMessageContaining("Order: " + 1L + " not found.");

        // then
        verify(orderRepository, never()).save(order);
    }

    @Test
    void createOrderTest() {
        //given
        User user = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                ACTIVE,
                Set.of(new Role("ADMIN"))
        );

        Product product1 = new Product(
                1L,
                "product1",
                "alias",
                "description",
                "band",
                200,
                2,
                true,
                null,
                null
        );

        Product product2 = new Product(
                2L,
                "product1",
                "alias",
                "description",
                "band",
                300,
                2,
                true,
                null,
                null
        );

        List<CartItem> cartItems = List.of(
                new CartItem(
                        1L,
                        (short) 2,
                        user,
                        product1
                ),
                new CartItem(
                        2L,
                        (short) 1,
                        user,
                        product2
                ));

        Order expectedOrder = new Order(
                1L,
                LocalDate.of(2022, 1, 1),
                700,
                user,
                null,
                PENDING
        );
        given(orderRepository.save(any(Order.class))).willReturn(expectedOrder);

        //when
        Order actualOrder = orderService.createOrder(user, cartItems);

        // then
        verify(orderRepository).save(any());
        AssertionsForClassTypes.assertThat(actualOrder).isEqualTo(expectedOrder);

    }

}

