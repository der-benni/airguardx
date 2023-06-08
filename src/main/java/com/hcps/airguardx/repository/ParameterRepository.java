package com.hcps.airguardx.repository;

import com.hcps.airguardx.model.ParameterModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParameterRepository extends CrudRepository<ParameterModel, Integer> {

    List<ParameterModel> findFirst60ByOrderByIdDesc();

    ParameterModel findFirst1ByOrderByIdDesc();

}