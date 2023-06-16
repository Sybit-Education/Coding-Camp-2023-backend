package de.sybit.sygotchi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle Global API Exceptions
     *
     * @param exception Thrown Exception
     * @param request   Requested API Web Request
     * @return ResponseEntity with Exception Modal and Status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandling(Exception exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        var exceptionStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        if (exceptionStatus != null) {
            status = exceptionStatus.value();
        }
        if (exception instanceof AccessDeniedException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        ApiExceptionModel exceptionModel =
                new ApiExceptionModel(exception.getMessage(), status.value(), request.getDescription(false));
        return new ResponseEntity<>(exceptionModel, status);
    }
}
