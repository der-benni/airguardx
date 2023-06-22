package com.hcps.airguardx.controller;

import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.model.StatusModel;
import com.hcps.airguardx.service.DataService;
import com.hcps.airguardx.service.StatusService;
import com.hcps.airguardx.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final StatusService statusService;
    private final DataService dataService;
    private final WeatherService weatherService;

    public StatusController(StatusService statusService, DataService dataService, WeatherService weatherService) {
        this.statusService = statusService;
        this.dataService = dataService;
        this.weatherService = weatherService;
    }

    @GetMapping("/roomStatus")
    public @ResponseBody StatusModel getRoomStatus() {

        DataModel dataModel = this.dataService.getLatestRecord();
        StatusModel statusModel = new StatusModel();

        if (dataModel.getTemperature() < 18 || dataModel.getTemperature() > 25) {
            statusModel.setStatus(0);
        } else if (dataModel.getHumidity() < 40 || dataModel.getHumidity() > 60) {
            statusModel.setStatus(0);
        } else {
            statusModel.setStatus(1);
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
