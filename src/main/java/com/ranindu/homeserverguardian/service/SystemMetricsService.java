package com.ranindu.homeserverguardian.service;

import com.ranindu.homeserverguardian.engine.DecisionEngine;
import com.ranindu.homeserverguardian.engine.MetricsEngine;
import com.ranindu.homeserverguardian.model.SystemMetrics;
import com.ranindu.homeserverguardian.model.SystemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class SystemMetricsService {

    private final MetricsEngine metricsEngine;
    private final DecisionEngine decisionEngine;

    public SystemMetrics getSystemMetrics() {
        double cpuTemp = metricsEngine.getCpuTemperature();
        double diskUsage = metricsEngine.getDiskUsage();

        return new SystemMetrics(cpuTemp, diskUsage);
    }

    public SystemStatus getSystemStatus() {
        SystemMetrics metrics = getSystemMetrics();
        return decisionEngine.analyze(metrics);
    }
    private double getCpuTeperature() {
        try{
            Process process = Runtime.getRuntime().exec("sysctl -a | grep -i temperature");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null){
                if(line.toLowerCase().contains("temperature")){
                    String temp = line.replaceAll("[0-9]","");
                    if (!temp.isEmpty()){
                        return Double.parseDouble(temp);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("CPU Temp Error" + e.getMessage());
        }
        return -1;
    }
    private double getDiskUsage() {
        try{
            Process process = Runtime.getRuntime().exec("df -h/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            reader.readLine();
            String line = reader.readLine();

            if(line != null ){
                String[] parts = line.split("\\s+");
                String usage = parts[4];
                return Double.parseDouble(usage.replace("%", ""));
            }
        } catch (Exception e) {
            System.out.println("Disk Error" + e.getMessage());
        }
        return -1;
    }
}
