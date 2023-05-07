package com.hcps.airguardx.controller;

import com.hcps.airguardx.service.ParameterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping("/name")
    public ResponseEntity<Object> setName(@RequestParam(value="name") String name) {
        parameterService.setName(name);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
