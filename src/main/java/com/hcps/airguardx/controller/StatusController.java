package com.hcps.airguardx.controller;

import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.service.DataService;
import com.hcps.airguardx.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final StatusService statusService;
    private final DataService dataService;

    public StatusController(StatusService statusService, DataService dataService) {
        this.statusService = statusService;
        this.dataService = dataService;
    }

    @GetMapping("/roomStatus")
    public ResponseEntity<Object> getRoomStatus() {

        DataModel dataModel = this.dataService.getLatestRecord();

        if (dataModel.getTemperature() < 20 || dataModel.getTemperature() > 23) {
            return new ResponseEntity<>("{'status': 0}", HttpStatus.OK);
        }

        if (dataModel.getHumidity() < 40 || dataModel.getHumidity() > 60) {
            return new ResponseEntity<>("{'status': 0}", HttpStatus.OK);
        }

        return new ResponseEntity<>("{'status': 1}", HttpStatus.OK);
    }

    @GetMapping("/sensorStatus")
    public ResponseEntity<Object> getSensorStatus() {

        if (statusService.isSensorStatus()) {
            return new ResponseEntity<>("{'status': 1}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{'status': 0}", HttpStatus.OK);
        }

    }

}
