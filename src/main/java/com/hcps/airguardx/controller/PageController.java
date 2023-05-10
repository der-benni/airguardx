package com.hcps.airguardx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/info")
    public String getInfo() {
        return "info";
    }

}
