package com.hcps.airguardx.repository;

import com.hcps.airguardx.model.DataModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataRepository extends CrudRepository<DataModel, Integer> {

    List<DataModel> findFirst12ByOrderByIdDesc();

    DataModel findFirst1ByOrderByIdDesc();

}