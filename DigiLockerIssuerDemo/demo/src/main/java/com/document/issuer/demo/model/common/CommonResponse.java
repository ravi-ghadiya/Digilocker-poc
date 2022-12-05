package com.document.issuer.demo.model.common;

import com.document.issuer.demo.model.DocDetails;
import com.document.issuer.demo.model.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonResponse {
    @JsonProperty("ResponseStatus")
    private ResponseStatus responseStatus;
    @JsonProperty("DocDetails")
    private DocDetails docDetails;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public DocDetails getDocDetails() {
        return docDetails;
    }

    public void setDocDetails(DocDetails docDetails) {
        this.docDetails = docDetails;
    }
}
