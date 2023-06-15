package com.hcps.airguardx.service;

import com.hcps.airguardx.model.DataDayModel;
import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.repository.DataDayRepository;
import com.hcps.airguardx.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
        return dataRepository.findFirst60ByOrderByIdDesc();
    }

    public Iterable<DataDayModel> getLastDayRecords() {
        return dataDayRepository.findFirst24ByOrderByIdDesc();
    }

    public void setDayData(DataModel data) {

        DataDayModel latest = this.dataDayRepository.findFirst1ByOrderByIdDesc();

        if (latest != null) {

            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

                String oldDate = hourFormat.format(dateFormat.parse(latest.getTimestamp()));
                String newDate = hourFormat.format(dateFormat.parse(data.getTimestamp()));

                if (!oldDate.equals(newDate)) {
                    this.dataDayRepository.save(convert(data));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            this.dataDayRepository.save(convert(data));
        }

    }

    private DataDayModel convert(DataModel dataModel) {

        DataDayModel newData = new DataDayModel();

        newData.setTemperature(dataModel.getTemperature());
        newData.setGas(dataModel.getGas());
        newData.setHumidity(dataModel.getHumidity());
        newData.setRelative_humidity(dataModel.getRelative_humidity());
        newData.setPressure(dataModel.getPressure());
        newData.setAltitude(dataModel.getAltitude());
        newData.setTimestamp(dataModel.getTimestamp());

        return newData;
    }

    public void setParameters(DataModel data) {
        dataRepository.save(data);
    }

}
