package com.hcps.airguardx.service;

import com.hcps.airguardx.model.DataModel;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private boolean sensorStatus = true;

    private final ProfileService profileService;
    private final DataService dataService;

    public StatusService(ProfileService profileService, DataService dataService) {
        this.profileService = profileService;
        this.dataService = dataService;
    }

    public void setSensorStatus(boolean status) {
        this.sensorStatus = status;
    }

    public boolean isSensorStatus() {
        return this.sensorStatus;
    }

    public boolean isValueCritical() {

        DataModel dataModel = this.dataService.getLatestRecord();
        int[] profileValues = profileService.getValues();

        if (dataModel.getTemperature() < profileValues[0] -2 || dataModel.getTemperature() > profileValues[1] + 2) {
            return true;
        } else if (dataModel.getHumidity() < profileValues[2] - 5 || dataModel.getHumidity() > profileValues[3] + 5) {
            return true;
        } else return dataModel.getCo2() > 3000;

    }

}
