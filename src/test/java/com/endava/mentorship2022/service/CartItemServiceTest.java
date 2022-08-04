package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.CartItemException;
import com.endava.mentorship2022.model.CartItem;
import com.endava.mentorship2022.model.Product;
import com.endava.mentorship2022.model.Role;
import com.endava.mentorship2022.model.TechnicalDetail;
import com.endava.mentorship2022.model.User;
import com.endava.mentorship2022.model.UserStatus;
import com.endava.mentorship2022.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    private CartItemService cartItemService;

    User user;

    Product product1;

    Product product2;

    List<CartItem> cartItems;

    @BeforeEach
    void setUp() {
        cartItemService = new CartItemService(cartItemRepository, productService);

        user = new User(1L,
                "FirstName",
                "LastName",
                "first_name@gmail.com",
                "pass",
                "Str. add",
                "06546465645",
                LocalDate.now(),
                UserStatus.ACTIVE,
                Set.of(new Role("ADMIN")));

        product1 = new Product(1L,
                "Product 1",
                "product-1",
                "Description",
                "No-name",
                15.49F,
                1500,
                true,
                null,
                Set.of(new TechnicalDetail(1L, "Brand:", "No-name", new Product(1L)))
        );

        product2 = new Product(2L,
                "Product 2",
                "product-2",
                "Description",
                "No-name",
                45.86F,
                1000,
                true,
                null,
                Set.of(new TechnicalDetail(1L, "Release Date:", "10/10/2020", new Product(2L)))
        );

        cartItems = List.of(
                new CartItem(1L, (short) 2, user, product1),
                new CartItem(2L, (short) 1, user, product2)
        );
    }

    @Test
    void canFindCartItemsByUser() {
        // given
        given(cartItemRepository.findCartItemsByUserId(anyLong())).willReturn(cartItems);

        // when
        List<CartItem> actualCartItems = cartItemService.findCartItemsByUser(user);

        // then
        verify(cartItemRepository).findCartItemsByUserId(anyLong());
        assertThat(actualCartItems).isEqualTo(cartItems);
    }

    @Test
    void canAddNewProductToCart() {
        // given
        Product product3 = new Product(3L,
                "Product 3",
                "product-3",
                "Description",
                "No-name",
                75.86F,
                2000,
                true,
                null,
                Set.of(new TechnicalDetail(1L, "Type:", "no-type", new Product(3L)))
        );
        given(productService.findById(anyLong())).willReturn(product3);
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(null);
        CartItem newCartItem = new CartItem(3L, (short) 3, user, product3);
        given(cartItemRepository.save(any(CartItem.class))).willReturn(newCartItem);

        // when
        String actual = cartItemService.addProductToCart(user, product3.getId(), (short) 3);

        // then
        assertThat(actual).isEqualTo("Product " + product3.getName() + " x" + 3 + " has been added to the cart.");
    }

    @Test
    void canAddExistentProductToCart() {
        // given
        given(productService.findById(anyLong())).willReturn(product1);
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(cartItems.get(0));
        CartItem newCartItem = new CartItem(2L, (short) 5, user, product1);
        given(cartItemRepository.save(any(CartItem.class))).willReturn(newCartItem);

        // when
        String actual = cartItemService.addProductToCart(user, product1.getId(), (short) 3);

        // then
        assertThat(actual).isEqualTo("Product " + product1.getName() + " x" + 5 + " has been added to the cart.");
    }

    @Test
    void willThrowCannotAddProductToCartMaximumQuantityExceeded_addQuantityForExistent() {
        // given
        given(productService.findById(anyLong())).willReturn(product1);
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(cartItems.get(0));

        // when
        assertThatThrownBy(() -> cartItemService.addProductToCart(user, product1.getId(), (short) 14))
                .isInstanceOf(CartItemException.class)
                .hasMessageContaining("Could not add more " + 14 + " item(s) " +
                        " because there's already " + 2 + " item(s) " +
                        "in your shopping cart. Maximum allowed quantity is 15.");

        // then
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void willThrowCannotAddProductToCartMaximumQuantityExceeded_addQuantity() {
        // given

        // when
        assertThatThrownBy(() -> cartItemService.addProductToCart(user, product1.getId(), (short) 20))
                .isInstanceOf(CartItemException.class)
                .hasMessageContaining("Could not add " + 20 + " items " +
                        "to your shopping cart. Maximum allowed quantity is 15.");

        // then
        verify(productService, never()).findById(anyLong());
        verify(cartItemRepository, never()).findCartItemByUserIdAndProductId(anyLong(), anyLong());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void canUpdateProductQuantityInCart() {
        // given
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(cartItems.get(0));

        // when
        String actual = cartItemService.updateProductQuantity(user, product1.getId(), (short) 3);

        // then
        verify(cartItemRepository).updateProductQuantity(anyShort(), anyLong(), anyLong());
        assertThat(actual).isEqualTo("Quantity has been updated! New quantity: " + 3);
    }

    @Test
    void cannotUpdateProductQuantity_productIsNotInCart() {
        // given
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(null);

        // when
        String actual = cartItemService.updateProductQuantity(user, product1.getId(), (short) 3);

        // then
        assertThat(actual).isEqualTo("The product is not in the cart.");
        verify(cartItemRepository, never()).updateProductQuantity(anyShort(), anyLong(), anyLong());
    }

    @Test
    void willThrowMaximumQuantityAllowedExceededWhenUpdateProductQuantity() {
        // given
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(cartItems.get(0));

        // when
        assertThatThrownBy(() -> cartItemService.updateProductQuantity(user, product1.getId(), (short) 18))
                .isInstanceOf(CartItemException.class)
                .hasMessageContaining("Could not update quantity to " + 18 +
                        ". Maximum allowed quantity is 15.");

        // then
        verify(cartItemRepository, never()).updateProductQuantity(anyShort(), anyLong(), anyLong());
    }

    @Test
    void canRemoveProductFromCart() {
        // given
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(cartItems.get(0));

        // when
        String actual = cartItemService.removeProductFromCart(user, product1.getId());

        // then
        verify(cartItemRepository).deleteCartItemByUserIdAndProductId(anyLong(), anyLong());
        assertThat(actual).isEqualTo("The product has been removed from your shopping cart.");
    }

    @Test
    void cannotRemoveProduct_notFoundInCart() {
        // given
        given(cartItemRepository.findCartItemByUserIdAndProductId(anyLong(), anyLong())).willReturn(null);

        // when
        String actual = cartItemService.removeProductFromCart(user, 55L);

        // then
        assertThat(actual).isEqualTo("The product is not in the cart.");
        verify(cartItemRepository, never()).deleteCartItemByUserIdAndProductId(anyLong(), anyLong());
    }

    @Test
    void canDeleteCartByUser() {
        // given
        given(cartItemRepository.findCartItemsByUserId(anyLong())).willReturn(cartItems);

        // when
        String actual = cartItemService.deleteCartByUser(user);

        // then
        assertThat(actual).isEqualTo("Cart has been deleted!");
    }

    @Test
    void cannotDeleteCartByUser_cartIsEmpty() {
        // given
        given(cartItemRepository.findCartItemsByUserId(anyLong())).willReturn(Collections.emptyList());

        // when
        String actual = cartItemService.deleteCartByUser(user);

        // then
        verify(cartItemRepository, never()).deleteCartItemsByUserId(anyLong());
        assertThat(actual).isEqualTo("Cart is already empty");
    }
}