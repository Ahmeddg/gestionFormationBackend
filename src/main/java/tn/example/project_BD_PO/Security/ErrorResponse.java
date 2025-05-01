package tn.example.project_BD_PO.Security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final String errorCode;
    private Map<String, String> fieldErrors;
    private List<String> globalErrors;

    public static ErrorResponse createBasicError(HttpStatus status, String message, String errorCode) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    public static ErrorResponse createValidationError(
            HttpStatus status,
            String message,
            String errorCode,
            Map<String, String> fieldErrors,
            List<String> globalErrors
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .errorCode(errorCode)
                .fieldErrors(fieldErrors)
                .globalErrors(globalErrors)
                .build();
    }
}