package com.hcps.airguardx.service;

import com.hcps.airguardx.model.DataDayModel;
import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.repository.DataDayRepository;
import com.hcps.airguardx.repository.DataRepository;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final DataDayRepository dataDayRepository;

    public DataService(DataRepository dataRepository, DataDayRepository dataDayRepository) {
        this.dataRepository = dataRepository;
        this.dataDayRepository = dataDayRepository;
    }

    public DataModel getLatestRecord() {
        return dataRepository.findFirst1ByOrderByIdDesc();
    }

    public Iterable<DataModel> getLastSixtyRecords() {
        return dataRepository.findFirst12ByOrderByIdDesc();
    }

    public Iterable<DataDayModel> getLastDayRecords() {
        return dataDayRepository.findFirst24ByOrderByIdDesc();
    }

    public void setDayData(DataModel data) {

        DataModel latest = this.dataRepository.findFirst1ByOrderByIdDesc();

        if (latest != null) {

            String oldDate = latest.getTimestamp().substring(11, 13);
            String newDate = data.getTimestamp().substring(11, 13);

            if (!oldDate.equals(newDate)) {
                this.dataDayRepository.save(convert(data));
            }

        } else {
            this.dataDayRepository.save(convert(data));
        }

    }

    private DataDayModel convert(DataModel dataModel) {

        DataDayModel newData = new DataDayModel();

        newData.setTemperature(dataModel.getTemperature());
        newData.setCo2(dataModel.getCo2());
        newData.setHumidity(dataModel.getHumidity());
        newData.setTimestamp(dataModel.getTimestamp());

        return newData;
    }

    public void setParameters(DataModel data) {
        dataRepository.save(data);
    }

}
