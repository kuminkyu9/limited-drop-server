package com.github.kuminkyu9.limiteddropserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Server is running!"; // 브라우저에 이 글자가 뜨면 성공!
    }
}