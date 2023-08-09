package ru.practicum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptionException(final Exception e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @Getter
    private static class ApiError {
        String message;
        String reason;
        HttpStatus status;
        LocalDateTime timestamp;

        @Override
        public String toString() {
            return "ApiError{" +
                    "status=" + status +
                    ", reason='" + reason + '\'' +
                    ", message='" + message + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }

        ApiError(HttpStatus status,
                 String reason,
                 String message,
                 LocalDateTime timestamp) {
            this.message = message;
            this.reason = reason;
            this.status = status;
            this.timestamp = timestamp;
        }
    }
}
