package com.ranindu.homeserverguardian.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemActionExecutor {
    public static void execute(String action) {

        switch (action) {

            case "CLEAN_DISK":
                System.out.println("🧹 Cleaning disk...");
                // TODO: add real command
                break;

            case "THROTTLE_TASKS":
                System.out.println("🔥 Throttling tasks...");
                // TODO: add real command
                break;

            case "NO_ACTION":
                System.out.println("✅ No action needed");
                break;

            default:
                System.out.println("⚠️ Unknown action: " + action);
        }
    }
    private static String runCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            return "Executed: " + command + "\n" + output;

        } catch (Exception e) {
            return "Execution failed: " + e.getMessage();
        }
    }
}


