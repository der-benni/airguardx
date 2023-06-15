package com.hcps.airguardx.repository;

import com.hcps.airguardx.model.DataDayModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataDayRepository extends CrudRepository<DataDayModel, Integer> {

    List<DataDayModel> findFirst24ByOrderByIdDesc();
    DataDayModel findFirst1ByOrderByIdDesc();

}
