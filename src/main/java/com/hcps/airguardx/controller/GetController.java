package com.hcps.airguardx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController {

    @GetMapping("/")
    public String getMessage() {
        return "AirGuardX online!";
    }

}
