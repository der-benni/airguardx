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

    public String getActiveProfileName() {

        String profileName = "";

        if (this.activeProfile == 0) {
            profileName = "Wohn- / Arbeitszimmer";
        } else if (this.activeProfile == 1) {
            profileName = "Schlafzimmer";
        } else if (this.activeProfile == 2) {
            profileName = "Küche";
        } else if (this.activeProfile == 3) {
            profileName = "Badezimmer";
        } else {
            profileName = "Keller";
        }

        return profileName;
    }

    public void setActiveProfile(int activeProfile) {
        this.activeProfile = activeProfile;
    }
}
