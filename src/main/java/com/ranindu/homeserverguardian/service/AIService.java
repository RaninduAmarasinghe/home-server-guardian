package com.ranindu.homeserverguardian.service;

import com.ranindu.homeserverguardian.config.AIConfig;
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

    public String askAI(String prompt){
      Map<String, Object> request = new HashMap<>();
      request.put("model", aiConfig.getModel());
      request.put("prompt", prompt);
      request.put("stream", false);

        Map response = restTemplate.postForObject(
                aiConfig.getUrl(),
                request,
                Map.class
        );
      return response.get("response").toString();
    }
}
