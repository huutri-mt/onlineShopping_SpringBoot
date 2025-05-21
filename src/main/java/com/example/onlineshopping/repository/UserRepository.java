package com.example.onlineshopping.repository;

import com.example.onlineshopping.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.email = :email, u.fullname = :fullname where u.id = :id")
    int updateUser(String email, String fullname, int id);
}
