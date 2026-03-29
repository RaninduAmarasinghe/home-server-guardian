package com.ranindu.homeserverguardian.controller;

import com.ranindu.homeserverguardian.model.SystemAction;
import com.ranindu.homeserverguardian.model.SystemMetrics;
import com.ranindu.homeserverguardian.model.SystemStatus;
import com.ranindu.homeserverguardian.service.SystemMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SystemMetricsController {

    private final SystemMetricsService systemMetricsService;

    @GetMapping("/metrics")
    public SystemMetrics getMetrics() {
        return systemMetricsService.getSystemMetrics();
    }

    @GetMapping("/status")
    public SystemStatus getStatus() {
        return systemMetricsService.getSystemStatus();
    }

    @GetMapping("/action")
    public SystemAction getAction() {
        return systemMetricsService.getSystemAction();
    }

    @GetMapping("/execute")
    public String executeAction() {
        return systemMetricsService.executeSystemAction();
    }
}
