package com.document.issuer.demo.model;

public enum EDocType {
    OFLTR(5, "OFLTR");

    private long docTypeId;
    private String name;

    EDocType(long docTypeId, String name) {
        this.docTypeId = docTypeId;
        this.name = name;
    }

    public long getValue() {
        return docTypeId;
    }

    public String getName() {
        return name;
    }
}