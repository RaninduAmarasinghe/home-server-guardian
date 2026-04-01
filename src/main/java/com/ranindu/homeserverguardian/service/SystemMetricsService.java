package com.ranindu.homeserverguardian.service;

import com.ranindu.homeserverguardian.engine.ActionEngine;
import com.ranindu.homeserverguardian.engine.DecisionEngine;
import com.ranindu.homeserverguardian.engine.MetricsEngine;
import com.ranindu.homeserverguardian.model.AIResponse;
import com.ranindu.homeserverguardian.model.SystemAction;
import com.ranindu.homeserverguardian.model.SystemMetrics;
import com.ranindu.homeserverguardian.model.SystemStatus;
import com.ranindu.homeserverguardian.util.SystemActionExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.BufferedReader;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SystemMetricsService {
    private String lastStatus = "";
    private long lastSentTime = 0;

    private final AIService aiService;
    private final MetricsEngine metricsEngine;
    private final DecisionEngine decisionEngine;
    private final ActionEngine actionEngine;
    private final TelegramService telegramService;

    public SystemMetrics getSystemMetrics() {

        double cpuUsage = metricsEngine.getCpuUsage();
        double cpuTemp = metricsEngine.getCpuTemperature();
        double diskUsage = metricsEngine.getDiskUsage();

        return new SystemMetrics(cpuUsage, cpuTemp, diskUsage);
    }

    public SystemStatus getSystemStatus() {
        SystemMetrics metrics = getSystemMetrics();
        return decisionEngine.analyze(metrics);
    }

    public SystemAction getSystemAction() {
        SystemStatus status = getSystemStatus();
        return actionEngine.decideAction(status);
    }

    public String executeSystemAction() {

        SystemMetrics metrics = getSystemMetrics();

        double cpuUsage = metrics.getCpuUsage();
        double cpuTemp = metrics.getCpuTemperature();
        double diskUsage = metrics.getDiskUsage();

        String cpuUsageDisplay = (cpuUsage <= 0)
                ? "Idle / N/A"
                : String.format("%.2f", cpuUsage) + "%";

        String cpuTempDisplay = (cpuTemp < 0)
                ? "N/A ❌"
                : cpuTemp + "°C";

        String diskDisplay = String.format("%.2f", diskUsage) + "%";

        String prompt =
                "System Metrics:\n" +
                        "CPU Usage: " + metrics.getCpuUsage() + "%\n" +
                        "CPU Temp: " + metrics.getCpuTemperature() + "\n" +
                        "Disk Usage: " + metrics.getDiskUsage() + "%\n\n" +
                        "Respond ONLY in JSON format:\n" +
                        "{ \"status\": \"HEALTHY | WARNING | CRITICAL\", " +
                        "\"message\": \"short message\", " +
                        "\"action\": \"NO_ACTION | CLEAN_DISK | THROTTLE_TASKS\" }\n" +
                        "Rules:\n" +
                        "- If CPU > 80 → THROTTLE_TASKS\n" +
                        "- If Disk > 80 → CLEAN_DISK\n" +
                        "- If normal → NO_ACTION";
        AIResponse ai = aiService.askAI(prompt);

        SystemActionExecutor.execute(ai.getAction());

        String message =
                "🤖 AI System Report\n\n" +
                        "⚙️ CPU Usage: " + cpuUsageDisplay + "\n" +
                        "🔥 CPU Temp: " + cpuTempDisplay + "\n" +
                        "💾 Disk Usage: " + diskDisplay + "\n\n" +
                        "Status: " + ai.getStatus() + "\n" +
                        "Analysis: " + ai.getMessage() + "\n" +
                        "Action: " + ai.getAction() + "\n\n" +
                        "Choose action:";

        long now = System.currentTimeMillis();

        if (!ai.getStatus().equals(lastStatus) || (now - lastSentTime > 60000)) {

            telegramService.sendMessageWithButtons(message);

            lastStatus = ai.getStatus();
            lastSentTime = now;
        }

        return "AI Telegram notification sent";
    }

    private double getCpuTemperature() {
        try{
            Process process = Runtime.getRuntime().exec("sysctl -a | grep -i temperature");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null){
                if(line.toLowerCase().contains("temperature")){
                    String temp = line.replaceAll("[^0-9.]", "");
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

    @Scheduled(fixedRate = 30000) // every 1 minute
    public void monitorSystem() {
        System.out.println("⏱ Scheduler running...");
        executeSystemAction();
    }
}
