package com.hcps.airguardx.service;

import org.springframework.stereotype.Service;

@Service
public class ParameterService {

    // todo: push data to database

    private Object data = null;

    public Object getParameters() {
        return this.data;
    }

    public void setParameters(Object data) {
        this.data = data;
    }

}
