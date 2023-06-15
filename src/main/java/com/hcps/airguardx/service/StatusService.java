package com.hcps.airguardx.service;

import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private boolean sensorStatus = true;

    public void setSensorStatus(boolean status) {
        this.sensorStatus = status;
    }

    public boolean isSensorStatus() {
        return this.sensorStatus;
    }
}
