package com.hcps.airguardx.controller;

import com.hcps.airguardx.service.ParameterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping("/parameters")
    public ResponseEntity<Object> setParameters(@RequestBody Object data) {

        System.out.println(data);
        parameterService.setParameters(data);

        return new ResponseEntity<>("Success: " + data, HttpStatus.OK);
    }

    @GetMapping("/parameters")
    public ResponseEntity<Object> getParameters() {

        Object data = parameterService.getParameters();
        System.out.println(data);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
