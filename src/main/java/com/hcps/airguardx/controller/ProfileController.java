package com.hcps.airguardx.controller;

import com.hcps.airguardx.service.ProfileService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/setProfile")
    public ResponseEntity<Object> setProfile(@RequestBody String jsonData) {

        JSONObject obj = new JSONObject(jsonData);
        profileService.setActiveProfile(obj.getInt("profile"));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getProfileValues")
    public int[] getProfileValues() {
        return profileService.getValues();
    }

}
