package com.ranindu.homeserverguardian.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemMetrics {
    private double cpuUsage;
    private double cpuTemperature;
    private double diskUsage;
}
