package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Cart;
import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategory(String category);

}