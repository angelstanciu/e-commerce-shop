package com.endava.mentorship2022.repository;

import com.endava.mentorship2022.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findCartItemsByUserId(long id);

    CartItem findCartItemByUserIdAndProductId(long userId, long productId);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.user.id = ?2 AND c.product.id = ?3")
    void updateProductQuantity(short quantity, long userId, long productId);

    void deleteCartItemByUserIdAndProductId(long userId, long productId);

    void deleteCartItemsByUserId(long userId);

}