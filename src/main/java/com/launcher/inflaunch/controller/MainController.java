package com.launcher.inflaunch.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;

@Controller
@RequestMapping("/")
public class MainController {
    PrintWriter pw = new PrintWriter(System.out);
    public MainController() { pw.println(getClass().getName() + "()생성"); }

    @RequestMapping("/main")
    public void main() {}

    // 현재 로그인한 정보 Authentication 보기 (디버깅 등 용도로 활용)
    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth() { // org.springframework.security.core.Authentication
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
