package com.document.issuer.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseStatus {
    @JsonProperty("Status")
    private String status;
    @JsonProperty("ts")
    private String timestamp;
    @JsonProperty("txn")
    private String txnId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
