package com.document.issuer.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents_master")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "doc_id")
    private String docId;
    @Column(name = "aadhar_number")
    private String aadharNumber;
    @Column(name = "digilocker_id")
    private String digiLockerId;
    @Column(name = "doc_name")
    private String docName;
    @Column(name = "doc_type")
    private String docType;

    @Column(name = "doc_extension")
    private String docExtension;
//    @Lob
//    @Column(name = "doc_data")
//    private byte[] docData;

    @Column(name = "doc_uri")
    private String docUri;
    @Column(name = "doc_path")
    private String docPath;

    @Column(name = "created_at")
    private Date createdDate = new Date();
    @Column(name = "updated_at")
    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getDigiLockerId() {
        return digiLockerId;
    }

    public void setDigiLockerId(String digiLockerId) {
        this.digiLockerId = digiLockerId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocUri() {
        return docUri;
    }

    public void setDocUri(String docUri) {
        this.docUri = docUri;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDocExtension() {
        return docExtension;
    }

    public void setDocExtension(String docExtension) {
        this.docExtension = docExtension;
    }
}
