package com.document.issuer.demo.model;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class UploadDocRequest {
    @NotBlank(message = "{NotEmpty.aadhar}")
    String aadharNumber;
    MultipartFile file;
    @NotBlank(message = "{NotEmpty.doctype}")
    String docType;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
