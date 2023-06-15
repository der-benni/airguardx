package com.hcps.airguardx.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DataDayModel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private float temperature;

    private float gas;

    private float humidity;

    private float relative_humidity;

    private float pressure;

    private float altitude;

    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getGas() {
        return gas;
    }

    public void setGas(float gas) {
        this.gas = gas;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getRelative_humidity() {
        return relative_humidity;
    }

    public void setRelative_humidity(float relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "DataModelDay{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", gas=" + gas +
                ", humidity=" + humidity +
                ", relative_humidity=" + relative_humidity +
                ", pressure=" + pressure +
                ", altitude=" + altitude +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
