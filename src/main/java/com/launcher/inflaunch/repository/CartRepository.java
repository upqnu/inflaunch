package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Cart;
import com.launcher.inflaunch.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserOrderByIdDesc(User user);
}