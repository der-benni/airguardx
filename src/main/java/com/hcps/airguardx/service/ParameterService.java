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

    public Iterable<ParameterModel> getParameters() {
        return parameterRepository.findAll();
    }

    public void setParameters(ParameterModel data) {
        parameterRepository.save(data);
    }

}
