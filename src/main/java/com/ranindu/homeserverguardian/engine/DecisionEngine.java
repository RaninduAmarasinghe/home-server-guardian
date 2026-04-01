package com.ranindu.homeserverguardian.engine;

import com.ranindu.homeserverguardian.model.SystemMetrics;
import com.ranindu.homeserverguardian.model.SystemStatus;
import org.springframework.stereotype.Component;

@Component
public class DecisionEngine {

    public SystemStatus analyze(SystemMetrics systemMetrics) {
      if (systemMetrics.getCpuTemperature() > 80 && systemMetrics.getCpuTemperature() > 90 ) {
          return new SystemStatus(
                  "CRITICAL",
                  "CPU temperature is high and disk is almost full"
          );
      }



        if(systemMetrics.getCpuTemperature () > 80 ){
            return new SystemStatus(
                    "CRITICAL",
              "CPU temperature is too high"
            );
        }

        if(systemMetrics.getDiskUsage() > 90){
            return new SystemStatus(
              "WARNING",
              "Disk usage is almost full"
            );
        }

        return new SystemStatus(
                "HEALTHY",
                "System is running normally"
        );
    }
}
