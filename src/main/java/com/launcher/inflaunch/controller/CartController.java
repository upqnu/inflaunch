package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;

@Controller
@RequestMapping("/")
public class CartController {
    public CartController() {
        System.out.println(getClass().getName() + "() 생성");
    }

    @Autowired
    private CartService cartService;

    @GetMapping("/carts")
    public void myCart(Model model) {
        model.addAttribute("carts", cartService.carts());
    }

    @PostMapping("/carts")
    public void putInCart() {

    }
}
