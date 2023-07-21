package com.launcher.inflaunch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;

@Controller
@RequestMapping("/admin")
public class AdminController {
    PrintWriter pw = new PrintWriter(System.out);
    public AdminController() { pw.println(getClass().getName() + "()생성"); }

    @RequestMapping("/income")
    public void income() {}
}
