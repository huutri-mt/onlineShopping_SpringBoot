package com.example.onlineshopping.repository;

import com.example.onlineshopping.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findByCartIdAndProductId(int cartId, int productId);
    List<CartItem> findByCartId(int cartId);
    CartItem save(CartItem cartItem);
    @Modifying
    @Transactional
    @Query("delete from CartItem c where c.cartId = ?1")
    void deleteByCartItem(int cartId);

}

