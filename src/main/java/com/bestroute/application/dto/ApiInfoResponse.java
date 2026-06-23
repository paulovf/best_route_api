package com.bestroute.application.dto;

import java.time.LocalDateTime;

public record ApiInfoResponse(
    String name,
    String version,
    String status,
    LocalDateTime timestamp
) {}
