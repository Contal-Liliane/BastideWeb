package pharmacie.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "pharmacie")
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

        String errorMessage = ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));

        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,
            "Validation error: " + errorMessage,
            errorMessage
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        String specificMessage = ex.getMostSpecificCause().getMessage();

        ApiError apiError = new ApiError(
            HttpStatus.CONFLICT,
            "Data integrity violation",
            "The operation could not be completed due to a data integrity violation: " + specificMessage
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {

        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND,
            "Resource not found",
            ex.getMessage()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {

        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,
            "Illegal state error",
            ex.getMessage()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {

        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,
            "Illegal argument error",
            ex.getMessage()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {

        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error",
            ex.getMessage()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
