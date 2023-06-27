package com.hcps.airguardx.controller;

import com.hcps.airguardx.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ProfileService profileService;

    public IndexController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {

        int activeProfile = profileService.getActiveProfile();

        String profileName = "";

        if (activeProfile == 0) {
            profileName = "Wohn- / Arbeitszimmer";
        } else if (activeProfile == 1) {
            profileName = "Schlafzimmer";
        } else if (activeProfile == 2) {
            profileName = "Küche";
        } else if (activeProfile == 3) {
            profileName = "Badezimmer";
        } else {
            profileName = "Keller";
        }

        model.addAttribute("profile", profileName);

        return "index";
    }

    @GetMapping("/info")
    public String getInfo() {
        return "info";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        model.addAttribute("profile", profileService.getActiveProfile());
        return "settings";
    }

}
