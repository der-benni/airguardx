package com.hcps.airguardx.config;

import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.service.DataService;
import com.hcps.airguardx.service.StatusService;
import com.hcps.airguardx.service.TelegramService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SpringConfig {

    private final DataService dataService;
    private final TelegramService telegramService;
    private final StatusService statusService;
    private DataModel dataModel = null;

    public SpringConfig(DataService dataService, TelegramService telegramService, StatusService statusService) {
        this.dataService = dataService;
        this.telegramService = telegramService;
        this.statusService = statusService;
    }

    // check every 10 minutes if sensor is sending data
    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateTask() {

        if (dataModel != null && this.dataModel.toString().equals(this.dataService.getLatestRecord().toString())) {
            // todo: enable sending data
            // this.telegramService.sendToTelegram("SensorNotSending!");
            this.statusService.setSensorStatus(false);
        } else {
            this.statusService.setSensorStatus(true);
        }

        dataModel = dataService.getLatestRecord();

    }

}
