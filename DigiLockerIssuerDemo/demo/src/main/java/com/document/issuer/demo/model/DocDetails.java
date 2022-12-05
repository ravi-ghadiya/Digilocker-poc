package com.document.issuer.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocDetails {
    @JsonProperty("DocType")
    private String docType;
    @JsonProperty("DigiLockerId")
    private String digiLockerId;
    @JsonProperty("UID")
    private String uid;
    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("DOB")
    private String dob;
    @JsonProperty("Photo")
    private String photo;
    @JsonProperty("UDF1")
    private String udf1;
    @JsonProperty("URI")
    private String docUri;
    @JsonProperty("DocContent")
    private String docContent;
    @JsonProperty("DataContent")
    private String dataContent;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDigiLockerId() {
        return digiLockerId;
    }

    public void setDigiLockerId(String digiLockerId) {
        this.digiLockerId = digiLockerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getDocUri() {
        return docUri;
    }

    public void setDocUri(String docUri) {
        this.docUri = docUri;
    }

    public String getDocContent() {
        return docContent;
    }

    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}
