package com.document.issuer.demo.rest;

import com.document.issuer.demo.constant.RestURI;
import com.document.issuer.demo.constant.StringPool;
import com.document.issuer.demo.entity.Document;
import com.document.issuer.demo.model.PullDocRequest;
import com.document.issuer.demo.model.PullURIRequest;
import com.document.issuer.demo.model.common.CommonRequest;
import com.document.issuer.demo.model.common.CommonResponse;
import com.document.issuer.demo.model.UploadDocRequest;
import com.document.issuer.demo.services.DigiLockerDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class DigiLockerDocumentResource {

    private final DigiLockerDocumentService digiLockerDocumentService;

    @Autowired
    public DigiLockerDocumentResource(DigiLockerDocumentService digiLockerDocumentService) {
        this.digiLockerDocumentService = digiLockerDocumentService;
    }

    @PostMapping(path = RestURI.PULL_URI_REQUEST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public CommonResponse pullDocumentURI(@RequestHeader(StringPool.DIGILOCKER_HMAC_HEADER) String authCode, @RequestBody PullURIRequest request) {
        System.out.println(request);
        return digiLockerDocumentService.pullDocumentURI(authCode, request);
    }

    @PostMapping(path = RestURI.PULL_DOCUMENT_REQUEST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public CommonResponse pullDocument(@RequestHeader(StringPool.DIGILOCKER_HMAC_HEADER) String authCode, @RequestBody PullDocRequest request){
        return digiLockerDocumentService.pullDocumentContent(authCode, request);
    }

    @PostMapping(value = "/document/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public Document uploadDocument(@Valid @ModelAttribute UploadDocRequest uploadDocRequest){
        return digiLockerDocumentService.saveDocument(uploadDocRequest);
    }
}
