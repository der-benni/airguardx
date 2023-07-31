package com.hcps.airguardx.controller;

import com.hcps.airguardx.model.StatusModel;
import com.hcps.airguardx.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/roomStatus")
    public @ResponseBody StatusModel getRoomStatus() {

        StatusModel statusModel = new StatusModel();

        if (statusService.isValueCritical()) {
            statusModel.setStatus(1);
        } else {
            statusModel.setStatus(0);
        }

        return statusModel;
    }

    @GetMapping("/sensorStatus")
    public @ResponseBody StatusModel getSensorStatus() {

        StatusModel statusModel = new StatusModel();

        if (statusService.isSensorStatus()) {
            statusModel.setStatus(1);
        } else {
            statusModel.setStatus(0);
        }

        return statusModel;
    }

}
