package com.hcps.airguardx.controller;

import com.hcps.airguardx.model.DataDayModel;
import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.service.DataService;
import com.hcps.airguardx.service.StatusService;
import com.hcps.airguardx.service.TelegramService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataController {

    private final DataService dataService;
    private final StatusService statusService;
    private final TelegramService telegramService;

    public DataController(DataService dataService, StatusService statusService, TelegramService telegramService) {
        this.dataService = dataService;
        this.statusService = statusService;
        this.telegramService = telegramService;
    }

    @PostMapping("/parameters")
    public ResponseEntity<Object> setParameters(@RequestBody String jsonData) {

        JSONObject obj = new JSONObject(jsonData);

        DataModel data = new DataModel();

        data.setTemperature(obj.getFloat("temperature"));
        data.setCo2(obj.getFloat("co2"));
        data.setHumidity(obj.getFloat("humidity"));
        data.setTimestamp(obj.getString("timestamp"));

        dataService.setParameters(data);
        dataService.setDayData(data);
        this.statusService.setSensorStatus(true);

        if (statusService.isValueCritical()) {
            telegramService.sendToTelegram("⚠️ Handlungsbedarf - Wertüberschreitung!");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/latestRecord")
    public @ResponseBody DataModel getLastRecord() {
        return dataService.getLatestRecord();
    }

    @GetMapping("/hourRecords")
    public @ResponseBody Iterable<DataModel> getLastSixtyRecords() {
        return dataService.getLastSixtyRecords();
    }

    @GetMapping("/dayRecords")
    public @ResponseBody Iterable<DataDayModel> getLastDayRecords() {
        return dataService.getLastDayRecords();
    }

}
