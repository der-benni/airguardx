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
    public String getIndex() {
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
