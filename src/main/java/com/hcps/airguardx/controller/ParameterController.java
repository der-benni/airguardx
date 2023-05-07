package com.hcps.airguardx.controller;

import com.hcps.airguardx.service.ParameterService;
import com.hcps.airguardx.service.TelegramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParameterController {

    private final ParameterService parameterService;
    private final TelegramService telegramService;

    public ParameterController(ParameterService parameterService, TelegramService telegramService) {
        this.parameterService = parameterService;
        this.telegramService = telegramService;
    }

    @PostMapping("/name")
    public ResponseEntity<Object> setName(@RequestParam(value="name") String name) {

        if (!name.isEmpty()) {
            parameterService.setName(name);
            telegramService.sendToTelegram(name);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
