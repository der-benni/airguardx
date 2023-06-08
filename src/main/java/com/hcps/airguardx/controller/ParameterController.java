package com.hcps.airguardx.controller;

import com.hcps.airguardx.model.ParameterModel;
import com.hcps.airguardx.service.ParameterService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping("/parameters")
    public ResponseEntity<Object> setParameters(@RequestBody String data) {

        JSONObject obj = new JSONObject(data);

        ParameterModel model = new ParameterModel();

        model.setTemperature(obj.getFloat("temperature"));
        model.setGas(obj.getFloat("gas"));
        model.setHumidity(obj.getFloat("humidity"));
        model.setRelative_humidity(obj.getFloat("relative_humidity"));
        model.setPressure(obj.getFloat("pressure"));
        model.setAltitude(obj.getFloat("altitude"));
        model.setTimestamp(obj.getString("timestamp"));

        parameterService.setParameters(model);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/lastHourRecords")
    public @ResponseBody Iterable<ParameterModel> getLastTenRecords() {
        return parameterService.getLastHourRecords();
    }

    @GetMapping("/lastRecord")
    public @ResponseBody ParameterModel getLastRecord() {
        return parameterService.getLastRecord();
    }

}
