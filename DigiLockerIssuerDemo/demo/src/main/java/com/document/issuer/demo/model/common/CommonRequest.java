package com.document.issuer.demo.model.common;

import com.document.issuer.demo.model.DocDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class CommonRequest implements Serializable {
    @JsonProperty("ver")
    private String apiVersion;
    @JsonProperty("ts")
    private String timestamp;
    @JsonProperty("txn")
    private String txnId;
    @JsonProperty("orgId")
    private String orgId;

    //Indicates the desired format of the certificate data to be sent in the response.
    //possible values: 1)xml 2)pdf 3)both
    //If the format attribute is not present in the request, then the API must return Base64 encoded PDF data in the response.
    @JsonProperty("format")
    private String docFormat;
    @JsonProperty("DocDetails")
    private DocDetails docDetails;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public DocDetails getDocDetails() {
        return docDetails;
    }

    public void setDocDetails(DocDetails docDetails) {
        this.docDetails = docDetails;
    }

    @Override
    public String toString() {
        return "CommonRequest{" +
                "apiVersion='" + apiVersion + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", txnId='" + txnId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", docFormat='" + docFormat + '\'' +
                ", docDetails=" + docDetails +
                '}';
    }
}
