package com.hcps.airguardx.service;

import org.springframework.stereotype.Service;

@Service
public class ParameterService {

    private String name = null;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
