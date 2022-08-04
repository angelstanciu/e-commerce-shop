package com.endava.mentorship2022.repository;

import com.endava.mentorship2022.model.Order;
import com.endava.mentorship2022.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}
