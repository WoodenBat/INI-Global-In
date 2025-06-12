package com.ini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "member/home";  // templates/member/home.html 로 연결됨
    }

    @GetMapping("/")
    public String root() {
        return "forward:/index.html";  // static/index.html 로 포워딩
    }
}




