package com.hcps.airguardx.service;

import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final int[][] values = {
            {40, 60, 20, 20},
            {40, 60, 16, 18},
            {50, 60, 18, 18},
            {50, 70, 23, 23},
            {50, 65, 10, 15}
    };

    private int activeProfile = 0;

    public int[] getValues() {
        return values[activeProfile];
    }

    public int getActiveProfile() {
        return this.activeProfile;
    }

    public void setActiveProfile(int activeProfile) {
        this.activeProfile = activeProfile;
    }
}
