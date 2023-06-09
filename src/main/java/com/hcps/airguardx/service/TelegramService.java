package com.hcps.airguardx.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class TelegramService {

    public void sendToTelegram(String text) {

        // template url
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

        // telegram api token
        String apiToken = System.getenv("TEL_API_KEY");

        // chatId of group chat
        String chatId = "-935276159";

        urlString = String.format(urlString, apiToken, chatId, URLEncoder.encode(text, StandardCharsets.UTF_8));

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
