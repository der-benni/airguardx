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
        model.setHumidity(obj.getFloat("humidity"));
        model.setVoc(obj.getFloat("voc"));
        model.setCo2(obj.getFloat("co2"));

        parameterService.setParameters(model);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/parameters")
    public @ResponseBody Iterable<ParameterModel> getParameters() {
        return parameterService.getParameters();
    }

}
