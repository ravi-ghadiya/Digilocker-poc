package com.document.issuer.demo.model;

import java.util.Map;

public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, String> fieldErrors;

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(Map<String, String> fieldErrors, int status, String message) {
        super(message, status);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}