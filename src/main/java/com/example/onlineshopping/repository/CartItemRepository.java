package com.example.onlineshopping.repository;

import com.example.onlineshopping.entity.Cart;
import com.example.onlineshopping.entity.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findByCartIdAndProductId(int cartId, int productId);

    List<CartItem> findByCartId(int cartId);

    CartItem save(CartItem cartItem);

    void deleteByCart(Cart cart);
}
