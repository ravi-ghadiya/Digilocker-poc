package com.document.issuer.demo.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigiLockerIssuerResponse<T> implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected String message;

    protected List<String> errorMessages;

    protected int status;

    protected T payload;

    /**
     *
     */
    public DigiLockerIssuerResponse() {

    }

    /**
     * @param payload
     */
    private DigiLockerIssuerResponse(T payload) {
        this.payload = payload;
    }

    public static <T> DigiLockerIssuerResponse<T> of(T payload) {
        return DigiLockerIssuerResponse.builder(payload).status(200).build();
    }

    public static <T> DigiLockerIssuerResponse<Object> of(String message) {
        return DigiLockerIssuerResponse.builder(new Object()).status(200).message(message).build();
    }

    public static <T> DigiLockerIssuerResponse<T> of(T payload, String msg) {
        return DigiLockerIssuerResponse.builder(payload).message(msg).status(200).build();
    }

    public static <T> DigiLockerIssuerResponse<Map<String, T>> of(String key, T payload, String msg) {
        Map<String, T> map = new HashMap<>(1);
        map.put(key, payload);
        return DigiLockerIssuerResponse.builder(map).message(msg).status(200).build();
    }

    public static <T> DigiLockerIssuerResponse<Map<String, T>> of(String key, T payload) {
        Map<String, T> map = new HashMap<>(1);
        map.put(key, payload);
        return DigiLockerIssuerResponse.builder(map).status(200).build();
    }

    /**
     * @param payload
     * @return builder
     */
    public static <T> Builder<T> builder(T payload) {
        return new Builder<>(payload);
    }

    /**
     * @return {@link String}
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return T
     */
    public T getPayload() {
        return payload;
    }

    /**
     * @param payload
     */
    public void setPayload(T payload) {
        this.payload = payload;
    }

    /**
     * @return errors
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessages
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * @param <T>
     * @author nitin
     */
    public static class Builder<T> {
        private final DigiLockerIssuerResponse<T> response;

        /**
         * @param payload
         */
        public Builder(T payload) {
            response = new DigiLockerIssuerResponse<>(payload);
        }

        /**
         * @param message
         * @return Builder
         */
        public Builder<T> message(String message) {
            response.message = message;
            return this;
        }


        /**
         * @param status
         * @return Builder
         */
        public Builder<T> status(int status) {
            response.status = status;
            return this;
        }


        /**
         * @param errorMessages
         * @return builder
         */
        public Builder<T> errors(List<String> errorMessages) {
            response.errorMessages = errorMessages;
            return this;
        }

        /**
         * @param payload
         * @return builder
         */
        public Builder<T> payload(T payload) {
            response.payload = payload;
            return this;
        }

        /**
         * @return {@link DigiLockerIssuerResponse}
         */
        public DigiLockerIssuerResponse<T> build() {
            return response;
        }
    }
}