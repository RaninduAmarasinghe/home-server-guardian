package com.ranindu.homeserverguardian.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SystemMetricsController {

    private final SystemMetricsController systemMetricsController;

    @GetMapping("/metrics")
    public SystemMetricsController getMetrics() {
        return systemMetricsController;
    }
}
