package com.hcps.airguardx.controller;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.service.DataService;
import com.hcps.airguardx.service.ProfileService;
import com.hcps.airguardx.service.WeatherService;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

@BotController
public class TelegramController implements TelegramMvcController {

    private final DataService dataService;
    private final WeatherService weatherService;

    private final ProfileService profileService;

    public TelegramController(DataService dataService, WeatherService weatherService, ProfileService profileService) {
        this.dataService = dataService;
        this.weatherService = weatherService;
        this.profileService = profileService;
    }

    @Override
    public String getToken() {
        return System.getenv("TEL_API_KEY");
    }

    @BotRequest(value = "/now", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getDataNow(User user, Chat chat) {

        DataModel dataModel = this.dataService.getLatestRecord();
        StringBuilder stb = new StringBuilder();
        int[] profile = this.profileService.getValues();

        if (dataModel != null) {
            String temperature = format(dataModel.getTemperature());
            String humidity = format(dataModel.getHumidity());
            String co2 = format(dataModel.getCo2());

            stb.append("Die aktuellen Daten für " + profileService.getActiveProfileName() + ":\n\n");
            stb.append("Temperatur: " + temperature + " °C (" + getStatusIndicator(dataModel.getTemperature(), profile[2], profile[3], 2) + ")\n");
            stb.append("Luftfeuchtigkeit: " + humidity + " % (" + getStatusIndicator(dataModel.getHumidity(), profile[0], profile[1], 5) + ")\n");
            stb.append("CO2: " + co2 + " ppm (" + getCo2Status(dataModel.getCo2()) + ")\n");
        }

        return new SendMessage(chat.id(), stb.toString());
    }

    @BotRequest(value = "/outside", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getDataOutside(User user, Chat chat) {

        StringBuilder stb = new StringBuilder();

        stb.append("Die aktuellen Wetterdaten:\n\n");
        stb.append("Temperatur: " + weatherService.getTemperature() + " °C\n");
        stb.append("Luftfeuchtigkeit: " + weatherService.getHumidity() + " %\n");

        return new SendMessage(chat.id(), stb.toString());
    }

    private String format(float data) {
        String temp = String.valueOf(data).replace('.', ',');
        return temp.substring(0, temp.indexOf(',') + 2);
    }

    private String getStatusIndicator(float value, int low, int high, int tolerance) {

        System.out.println(value + " " + high + " " + low + " " + tolerance);

        if (value >= low && value <= high) {
            return "🟢";
        } else if ((value >= low - tolerance && value < low) || (value > high && value <= high + tolerance)) {
            return "🟠";
        } else {
            return "🔴";
        }
    }

    private String getCo2Status(float value) {
        if (value  <= 1000) {
            return "🟢";
        } else if (value <= 2000) {
            return "🟠";
        } else {
            return "🔴";
        }
    }

}
