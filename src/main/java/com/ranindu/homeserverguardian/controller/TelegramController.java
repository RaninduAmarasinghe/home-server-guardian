package com.ranindu.homeserverguardian.controller;

import com.ranindu.homeserverguardian.service.SystemMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TelegramController {

    private final SystemMetricsService systemMetricsService;

    @PostMapping("/telegram/webhook")
    public void handleUpdate(@RequestBody Map<String,Object> update){
        if(update.containsKey("callback_query")){

            Map<String, Object> callback = (Map<String, Object>) update.get("callback_query");
            String data = (String) callback.get("data");

            if("APPROVE".equals(data)){
                systemMetricsService.executeSystemAction();
            }
            if("IGNORE".equals(data)){
                System.out.println("User ignored action");
            }

        }
    }
}
