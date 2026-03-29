package com.ranindu.homeserverguardian.service;

import com.ranindu.homeserverguardian.model.SystemMetrics;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class SystemMetricsService {

    public SystemMetrics getSystemMetrics() {
        double cpuTemp = getCpuTeperature();
        double diskUsage = getDiskUsage();

        return SystemMetrics.(cpuTemp, diskUsage);
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
