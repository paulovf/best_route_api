package com.bestroute.api.response;

import java.time.LocalDateTime;

public record ApiInfoResponse(String name, String version, String status, LocalDateTime timestamp) {
}
