package io.rachidassouani.fullstackjava.exception;

import java.time.LocalDateTime;

// todo we can use ProblemDetails api, https://www.sivalabs.in/spring-boot-3-error-reporting-using-problem-details/
public record ApiError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime) {
}
