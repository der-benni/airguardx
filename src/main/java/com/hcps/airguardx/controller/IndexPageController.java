package com.hcps.airguardx.controller;

import com.hcps.airguardx.service.ParameterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexPageController {

    private final ParameterService parameterService;

    public IndexPageController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("name", parameterService.getName());
        return "index";
    }

}
