package com.hcps.airguardx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/status")
    public ResponseEntity<Object> getStatus() {
        return new ResponseEntity<>("{'status': 0}", HttpStatus.OK);
    }

}
