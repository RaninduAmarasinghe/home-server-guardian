package com.ranindu.homeserverguardian.util;

public class SystemActionExecutor {
    public static String executeAction(String action) {
        switch (action) {
            case "CLEANUP_STORAGE":
                return runCommand("rm -rf /tmp/*");

            case "THROTTLE_TASKS":
                return runCommand("echo 'Simulating throttling...'");

            default:
                return "No action executed";
        }
    }

    private static String runCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            return "Executed:" + command;
        }catch (Exception e) {
            return"Excution faild: " + e.getMessage();
        }
    }
}


