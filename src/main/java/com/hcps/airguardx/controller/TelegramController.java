package com.hcps.airguardx.controller;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.hcps.airguardx.model.DataModel;
import com.hcps.airguardx.service.DataService;
import com.hcps.airguardx.service.WeatherService;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

@BotController
public class TelegramController implements TelegramMvcController {

    private final DataService dataService;
    private final WeatherService weatherService;

    public TelegramController(DataService dataService, WeatherService weatherService) {
        this.dataService = dataService;
        this.weatherService = weatherService;
    }

    @Override
    public String getToken() {
        return System.getenv("TEL_API_KEY");
    }

    @BotRequest(value = "/now", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getDataNow(User user, Chat chat) {

        DataModel dataModel = this.dataService.getLatestRecord();
        StringBuilder stb = new StringBuilder();

        if (dataModel != null) {
            String temperature = format(dataModel.getTemperature());
            String humidity = format(dataModel.getHumidity());

            stb.append("Hallo, " + user.firstName() + "!\n\n");
            stb.append("Die aktuellen Daten:\n");
            stb.append("🌡 " + temperature + "°C\n");
            stb.append("💧 " + humidity + "%\n");
        }

        return new SendMessage(chat.id(), stb.toString());
    }

    @BotRequest(value = "/outside", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest getDataOutside(User user, Chat chat) {

        StringBuilder stb = new StringBuilder();

        stb.append("Hallo, " + user.firstName() + "!\n\n");
        stb.append("Die aktuellen Wetterdaten:\n");
        stb.append("🌡 " + weatherService.getTemperature() + "°C\n");
        stb.append("💧 " + weatherService.getHumidity() + "%\n");

        return new SendMessage(chat.id(), stb.toString());
    }

    private String format(float data) {
        String temp = String.valueOf(data).replace('.', ',');
        return temp.substring(0, temp.indexOf(',') + 2);
    }

}
