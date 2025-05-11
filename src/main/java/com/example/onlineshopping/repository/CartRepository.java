package com.example.onlineshopping.repository;

import com.example.onlineshopping.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUserId(int userId);
    Cart save(Cart cart);
    @Query("SELECT c.user.id FROM Cart c WHERE c.id = :cartId")
    int findUserIdById( int cartId);

}
