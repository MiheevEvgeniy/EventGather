package ru.practicum.exceptions;

import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ConstraintViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(final Exception e) {
        return new ApiError(HttpStatus.CONFLICT,
                            "Integrity constraint has been violated.",
                            e.getMessage(),
                            LocalDateTime.now());
    }
    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundExceptionException(final Exception e) {
        return new ApiError(HttpStatus.NOT_FOUND,
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptionException(final Exception e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }


//
//    @ExceptionHandler({InvalidDataException.class, UnavailableItemException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleBadRequestException(final Exception e) {
//        return new ErrorResponse(e.getMessage(), e.getStackTrace());
//    }
//
//    @ExceptionHandler({UnsupportedStatusException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleUnsupportedStatusException(final UnsupportedStatusException e) {
//        return new ErrorResponse(e.getMessage(), e.getStackTrace());
//    }
//
//    @ExceptionHandler({OwnerAccessException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ErrorResponse handleForbiddenException(final Exception e) {
//        return new ErrorResponse(e.getMessage(), e.getStackTrace());
//    }
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
