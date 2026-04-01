package com.ranindu.homeserverguardian.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranindu.homeserverguardian.config.AIConfig;
import com.ranindu.homeserverguardian.model.AIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AIService {

    private final AIConfig aiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public AIResponse askAI(String prompt){

        Map<String, Object> request = new HashMap<>();
        request.put("model", aiConfig.getModel());
        request.put("prompt", prompt);
        request.put("stream", false);

        Map response = restTemplate.postForObject(
                aiConfig.getUrl(),
                request,
                Map.class
        );

        String aiText = response.get("response").toString();

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiText, AIResponse.class);
        } catch (Exception e){
            throw new RuntimeException("AI parsing failed: " + aiText);
        }
    }
}
