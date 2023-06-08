package com.hcps.airguardx.service;

import com.hcps.airguardx.model.ParameterModel;
import com.hcps.airguardx.repository.ParameterRepository;
import org.springframework.stereotype.Service;

@Service
public class ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    public Iterable<ParameterModel> getLastHourRecords() {
        return parameterRepository.findFirst60ByOrderByIdDesc();
    }

    public ParameterModel getLastRecord() {
        return parameterRepository.findFirst1ByOrderByIdDesc();
    }

    public void setParameters(ParameterModel data) {
        parameterRepository.save(data);
    }

}
