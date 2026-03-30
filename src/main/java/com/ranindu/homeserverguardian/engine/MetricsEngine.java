package com.ranindu.homeserverguardian.engine;

import org.springframework.stereotype.Component;

@Component
public class MetricsEngine {

    public double getDiskUsage() {
        return 91;
    }
    public double getCpuTemperature(){
        return 82;
    }
}
