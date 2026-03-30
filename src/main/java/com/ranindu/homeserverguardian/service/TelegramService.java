package com.ranindu.homeserverguardian.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessageWithButtons(String message) {

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{"
                + "\"chat_id\":\"" + chatId + "\","
                + "\"text\":\"" + message + "\","
                + "\"reply_markup\":{"
                + "\"inline_keyboard\":[["
                + "{\"text\":\"✅ Approve\",\"callback_data\":\"APPROVE\"},"
                + "{\"text\":\"❌ Ignore\",\"callback_data\":\"IGNORE\"}"
                + "]]"
                + "}"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForObject(url, request, String.class);
    }

}
