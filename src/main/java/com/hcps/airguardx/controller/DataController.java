package com.hcps.airguardx.controller;

import com.hcps.airguardx.model.DataDayModel;
import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.service.DataService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/data")
    public ResponseEntity<Object> setParameters(@RequestBody String jsonData) {

        JSONObject obj = new JSONObject(jsonData);

        DataModel data = new DataModel();

        data.setTemperature(obj.getFloat("temperature"));
        data.setGas(obj.getFloat("gas"));
        data.setHumidity(obj.getFloat("humidity"));
        data.setRelative_humidity(obj.getFloat("relative_humidity"));
        data.setPressure(obj.getFloat("pressure"));
        data.setAltitude(obj.getFloat("altitude"));
        data.setTimestamp(obj.getString("timestamp"));

        dataService.setParameters(data);
        dataService.setDayData(data);

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
