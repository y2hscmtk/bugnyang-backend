package com.winner_cat.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    public String adminP() {
        return "This is Admin Controller";
    }

    @GetMapping("/")
    public String mainP() {
        return "This is Main Controller";
    }
}
