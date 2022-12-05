package com.document.issuer.demo.config;

import com.document.issuer.demo.Exception.BadDataException;
import com.document.issuer.demo.Exception.SecurityException;
import com.document.issuer.demo.constant.StringPool;
import com.document.issuer.demo.model.ErrorResponse;
import com.document.issuer.demo.model.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExpertExceptionHandler {

    public static final String HANDLE_ENTITY_EXCEPTION_ERROR = "handleEntityException: Error";
    private final MessageSource messageSource;

    private static final String INTERNAL_SERVER_ERROR = "internal.server.error";

    /**
     * @param messageSource
     */
    @Autowired
    public ExpertExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private final Logger log = LoggerFactory.getLogger(ExpertExceptionHandler.class);

    /**
     * @param exception
     * @return ResponseEntity with error details
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleException(Exception exception) {
        log.error("Internal server error", exception);
        String message = messageSource.getMessage(INTERNAL_SERVER_ERROR, new Object[]{}, LocaleContextHolder.getLocale());
        Map<String, Object> map = new HashMap<>();
        map.put(StringPool.ERROR, new ErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DigiLockerIssuerResponse.builder(map)
                        .message(message).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
    }

    /**
     * @param exception
     * @return ResponseEntity with error details
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleException(SecurityException exception) {
        log.error("SecurityException", exception);
        Map<String, Object> map = new HashMap<>();
        String msg = "";
        try {
            msg = messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            msg = exception.getMessage();
        }
        map.put(StringPool.ERROR, new ErrorResponse(msg, HttpStatus.UNAUTHORIZED.value()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(DigiLockerIssuerResponse.builder(map).message(msg).status(HttpStatus.UNAUTHORIZED.value()).build());
    }

    /**
     * @param exception
     * @return ResponseEntity with error details
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class, BadDataException.class, PropertyReferenceException.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleEntityException(Exception exception) {
        log.error(HANDLE_ENTITY_EXCEPTION_ERROR, exception);
        String msg = "";
        try {
            msg = messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            if (exception instanceof HttpMessageNotReadableException) {
                msg = "Invalid data";
            } else {
                msg = exception.getMessage();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put(StringPool.ERROR, new ErrorResponse(msg, HttpStatus.BAD_REQUEST.value()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DigiLockerIssuerResponse.builder(map).message(msg).status(HttpStatus.BAD_REQUEST.value()).build());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleUnProcessableEntityException(Exception exception) {
        log.error(HANDLE_ENTITY_EXCEPTION_ERROR, exception);
        String msg = "";
        try {
            msg = messageSource.getMessage(INTERNAL_SERVER_ERROR, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            if (exception instanceof HttpMessageNotReadableException) {
                msg = "Invalid data";
            } else {
                msg = exception.getMessage();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put(StringPool.ERROR, new ErrorResponse(msg, HttpStatus.UNPROCESSABLE_ENTITY.value()));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(DigiLockerIssuerResponse.builder(map).message(msg).status(HttpStatus.UNPROCESSABLE_ENTITY.value()).build());
    }


    /**
     * @param exception
     * @return ResponseEntity with error details
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleNotFound(Exception exception) {
        log.error(HANDLE_ENTITY_EXCEPTION_ERROR, exception);
        Map<String, Object> map = new HashMap<>();
        String msg = "";
        try {
            msg = messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            msg = exception.getMessage();
        }
        map.put(StringPool.ERROR, new ErrorResponse(msg, HttpStatus.NOT_FOUND.value()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DigiLockerIssuerResponse.builder(map).message(msg).status(HttpStatus.NOT_FOUND.value()).build());
    }

    /**
     * @param exception
     * @return ResponseEntity with error details
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleValidation(MethodArgumentNotValidException exception) {
        log.error(HANDLE_ENTITY_EXCEPTION_ERROR, exception);
        List<FieldError> fieldErrors = exception.getBindingResult().getAllErrors().stream().map(error -> (FieldError) error).collect(Collectors.toList());
        return handleValidationError(fieldErrors);

    }

    /**
     * @param exception
     * @return ResponseEntity with error details
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleValidation(BindException exception) {
        log.error(HANDLE_ENTITY_EXCEPTION_ERROR, exception);
        List<FieldError> fieldErrors = exception.getBindingResult().getAllErrors().stream().map(error -> (FieldError) error).collect(Collectors.toList());
        return handleValidationError(fieldErrors);

    }

    /**
     * @param fieldErrors
     * @return ResponseEntity with error details
     */

    private ResponseEntity<DigiLockerIssuerResponse<Map<String, Object>>> handleValidationError(List<FieldError> fieldErrors) {

        Map<String, String> messages = new HashMap<>();
        fieldErrors.forEach(fieldError -> messages.put(fieldError.getField(), fieldError.getDefaultMessage()));
        Map<String, Object> map = new HashMap<>();
        map.put(StringPool.ERROR, new ValidationErrorResponse(messages, HttpStatus.BAD_REQUEST.value(), "Validation failed"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DigiLockerIssuerResponse.builder(map).message(messages.entrySet().iterator().next().getValue()).status(HttpStatus.BAD_REQUEST.value()).build());
    }

}