package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}