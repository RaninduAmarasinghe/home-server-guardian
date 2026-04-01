package com.ranindu.homeserverguardian.engine;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@Component
public class MetricsEngine {

    public double getDiskUsage() {
        File root = new File("/");

        long total = root.getTotalSpace();
        long free = root.getFreeSpace();

        return ((total - free) * 100.0) / total;
    }

    public double getCpuUsage() {

        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double cpuLoad = osBean.getSystemCpuLoad();

        if (cpuLoad < 0) {
            return -1; // not available
        }

        return cpuLoad * 100;
    }


    public double getCpuTemperature() {
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"/bin/sh", "-c", "sysctl -a | grep -i temperature"}
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("temperature")) {
                    String temp = line.replaceAll("[^0-9.]", "");
                    return Double.parseDouble(temp);
                }
            }

        } catch (Exception e) {
            System.out.println("CPU Temp Error: " + e.getMessage());
        }

        return -1;
    }
}
