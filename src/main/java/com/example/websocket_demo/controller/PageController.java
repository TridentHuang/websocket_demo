package com.example.websocket_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("hello")
    public String hello() {
        System.out.println("hello");
        return "Hello";
    }
}
