package com.ranindu.homeserverguardian.engine;

import com.ranindu.homeserverguardian.model.SystemAction;
import com.ranindu.homeserverguardian.model.SystemStatus;
import org.springframework.stereotype.Component;

@Component
public class ActionEngine {
 public SystemAction decideAction(SystemStatus status) {
     switch (status.getStatus()){
         case "CRITICAL":
             return new SystemAction(
               "THROTTLE_TASKS",
               "Pausing heavy background process to reduce CPU "
             );

         case "WARNING":
             return new SystemAction(
                     "CLEANUP_STORAGE",
                     "Cleaning unnecessary files to free disk space "
             );

         default:
             return new SystemAction(
                     "NO_ACTION",
                     "System is stable, no action needed"
             );
     }
 }
}
