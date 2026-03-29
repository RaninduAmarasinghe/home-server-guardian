package com.ranindu.homeserverguardian.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemStatus {
    private String status;
    private String message;
}
