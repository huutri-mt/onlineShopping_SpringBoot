package com.example.onlineshopping.repository;

import com.example.onlineshopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);

}
