package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.Cart;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.repository.CartRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;


    //myCartList
    public List<Cart> carts() {
        User user = U.getLoggedUser();
//        user = userRepository.findById(user.getId()).orElse(null);
        List<Cart> carts = cartRepository.findByUserOrderByIdDesc(user);
        return carts;
    }
}
