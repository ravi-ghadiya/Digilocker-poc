package com.document.issuer.demo.services;

import com.document.issuer.demo.Exception.BadDataException;
import com.document.issuer.demo.Exception.SecurityException;
import com.document.issuer.demo.constant.StringPool;
import com.document.issuer.demo.entity.Document;
import com.document.issuer.demo.model.*;
import com.document.issuer.demo.model.common.CommonResponse;
import com.document.issuer.demo.repository.DigiLockerDocumentRepository;
import com.document.issuer.demo.utils.RandomUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class DigiLockerDocumentService {

    private final DigiLockerDocumentRepository digiLockerDocumentRepository;
    private final String secretKey;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(DigiLockerDocumentService.class);

    private static String UPLOAD_DIR = "/home/ravi/projects/DigiLocker-poc/DigiLockerIssuerDemo/demo/src/main/resources/static";

    @Autowired
    public DigiLockerDocumentService(DigiLockerDocumentRepository digiLockerDocumentRepository,
                                     @Value("${digilocker.issuer.apiKey}") String secretKey,
                                     ObjectMapper objectMapper, XmlMapper xmlMapper) {
        this.digiLockerDocumentRepository = digiLockerDocumentRepository;
        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
    }

    public PullURIResponse pullDocumentURI(String authCode, PullURIRequest request) {
        authenticateRequest(authCode, request);

        if (Objects.isNull(request.getDocDetails())){
            LOGGER.debug("pullDocumentURI:: request does not contain DocDetails");
            throw new IllegalArgumentException("invalid.request");
        }
        ResponseStatus responseStatus = null;
        DocDetails docDetails = null;
        PullURIResponse pullURIResponse = null;
        try {
            switch (request.getDocDetails().getDocType()) {
                case "OFLTR":
                    String aadharNumber = request.getDocDetails().getUdf1();
                    if (Objects.isNull(aadharNumber)){
                        LOGGER.debug("pullDocumentURI:: udf1/aadharNumber not found in request.");
                        throw new BadDataException("invalid.request");
                    }
                    Document document = digiLockerDocumentRepository.findByAadharNumber(aadharNumber);
                    if (Objects.isNull(document)){
                        throw new BadDataException("");
                    }

                    pullURIResponse = new PullURIResponse();
                    responseStatus = new ResponseStatus();
                    responseStatus.setStatus("1");
                    responseStatus.setTimestamp(request.getTimestamp());
                    responseStatus.setTxnId(request.getTxnId());
                    pullURIResponse.setResponseStatus(responseStatus);

                    docDetails = new DocDetails();
                    docDetails.setDocUri(document.getDocUri());
                    docDetails.setDocType(request.getDocDetails().getDocType());
                    docDetails.setDigiLockerId(request.getDocDetails().getDigiLockerId());
                    docDetails.setUid(request.getDocDetails().getUid());
                    docDetails.setFullName(request.getDocDetails().getFullName());
                    docDetails.setDob(request.getDocDetails().getDob());
                    docDetails.setUdf1(request.getDocDetails().getUdf1());

                    setDocumentContent(request.getDocFormat(), docDetails, document.getDocPath());

                    pullURIResponse.setDocDetails(docDetails);
                    break;
            }
        } catch (Exception e){
            pullURIResponse = new PullURIResponse();
            responseStatus = new ResponseStatus();
            responseStatus.setStatus("0");
            responseStatus.setTimestamp(request.getTimestamp());
            responseStatus.setTxnId(request.getTxnId());
            pullURIResponse.setResponseStatus(responseStatus);

            docDetails = new DocDetails();
            docDetails.setDocType(request.getDocDetails().getDocType());
            docDetails.setDigiLockerId(request.getDocDetails().getDigiLockerId());
            docDetails.setUid(request.getDocDetails().getUid());
            docDetails.setFullName(request.getDocDetails().getFullName());
            docDetails.setDob(request.getDocDetails().getDob());
            docDetails.setUdf1(request.getDocDetails().getUdf1());
            docDetails.setDocContent("Document yet not Created !");
            pullURIResponse.setDocDetails(docDetails);

        }
        return pullURIResponse;
    }

    public CommonResponse pullDocumentContent(String authCode, PullDocRequest request) {
        authenticateRequest(authCode, request);

        if (Objects.isNull(request.getDocDetails())){
            LOGGER.debug("pullDocument:: request does not contain DocDetails");
            throw new BadDataException("invalid.request");
        }
        ResponseStatus responseStatus = null;
        DocDetails docDetails = null;
        CommonResponse commonResponse = null;
        try {
            switch (request.getDocDetails().getDocType()) {
                case "OFLTR":
                    String aadharNumber = request.getDocDetails().getUdf1();
                    if (Objects.isNull(aadharNumber)){
                        LOGGER.debug("pullDocument:: udf1/aadharNumber not found in request.");
                        throw new BadDataException("invalid.request");
                    }
                    Document document = digiLockerDocumentRepository.findByAadharNumber(aadharNumber);
                    commonResponse = new CommonResponse();

                    responseStatus = new ResponseStatus();
                    responseStatus.setStatus("1");
                    responseStatus.setTimestamp(request.getTimestamp());
                    responseStatus.setTxnId(request.getTxnId());
                    commonResponse.setResponseStatus(responseStatus);

                    docDetails = new DocDetails();

                    setDocumentContent(request.getDocFormat(), docDetails, document.getDocPath());
                    commonResponse.setDocDetails(docDetails);
                    break;
            }
        } catch (Exception e){
            commonResponse = new CommonResponse();
            responseStatus = new ResponseStatus();
            responseStatus.setStatus("0");
            responseStatus.setTimestamp(request.getTimestamp());
            responseStatus.setTxnId(request.getTxnId());
            commonResponse.setResponseStatus(responseStatus);

            docDetails = new DocDetails();
            docDetails.setDocContent("Document yet not Created !");
            commonResponse.setDocDetails(docDetails);
        }
        return commonResponse;
    }


    //request authentication
    // 1) calculate hmac of request body using SHA256 hashing algorithm and the API Key provided by the issuer as the hashing key.
    // 2) convert it to Base64
    // 3) match it with authCode received in request to ensure authenticity of request.
    private <T> void authenticateRequest(String authCode, T request) {
        String requestBody = null;
        try {
            requestBody = xmlMapper.writeValueAsString(request);
            LOGGER.debug("request = {}", request);
            LOGGER.debug("request = {}", xmlMapper.writeValueAsString(request));
            LOGGER.debug("request = {}", objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String hash = calculateHash(requestBody, secretKey);

        if (!hash.equals(authCode)){
            LOGGER.debug("pullDocumentURI:: authCode {} does not match with calculated hash {}", authCode, hash);
            throw new BadDataException("request authentication failed. invalid request");
        }
    }

    private static void setDocumentContent(String docFormat, DocDetails docDetails, String docPath) throws IOException {
        String encodedPdfDocStr = "";
        String encodedXmlDocStr = "";
        if (docFormat.equalsIgnoreCase("xml")){
            //TODO: convertXmlToBase64
            docDetails.setDataContent(encodedXmlDocStr);
        }
        else if (docFormat.equalsIgnoreCase("pdf")) {
            encodedPdfDocStr = convertPdfToBase64(docPath);
            docDetails.setDocContent(encodedPdfDocStr);
        }
        else if (docFormat.equalsIgnoreCase("both")) {
            //TODO: convertXmlToBase64
            encodedPdfDocStr = convertPdfToBase64(docPath);
            docDetails.setDataContent(encodedXmlDocStr);
            docDetails.setDocContent(encodedPdfDocStr);
        }
        else {
            encodedPdfDocStr = convertPdfToBase64(docPath);
            docDetails.setDocContent(encodedPdfDocStr);
        }
    }


    private String calculateHash(String data, String key) {
        String hash = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes()));
        } catch (Exception e){
            throw new SecurityException("Failed to calculate hmac-sha256", e);
        }
        return hash;
    }

    private static String convertPdfToBase64(String docpath) throws IOException {
        byte[] inFileBytes = Files.readAllBytes(Paths.get(docpath));
        byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
        String encodedDocStr = new String(encoded, StandardCharsets.UTF_8);
        LOGGER.debug("pullDocumentURI: Base64 byte encoded contents of PDF file {}", encodedDocStr);
        return encodedDocStr;
    }

    public Document saveDocument(UploadDocRequest uploadDocRequest) {
        MultipartFile file = uploadDocRequest.getFile();
        try {
            if (file.isEmpty() && Objects.isNull(uploadDocRequest.getAadharNumber())) {
                throw new BadDataException("invalid.request");
            }
            //file upload to UPLOAD_DIR
            String docPath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
            long res = Files.copy(file.getInputStream(), Paths.get(docPath), StandardCopyOption.REPLACE_EXISTING);

            String docUri = null;
            String docId = null;
            if (res>0){
                Document document = new Document();
                if (uploadDocRequest.getDocType().equals(EDocType.OFLTR.getName())){
                    docId = RandomUtils.generateAlphaString(10);
                    docUri = StringPool.ISSUER_ID + "-" + StringPool.DOC_TYPE + "-" + docId;
                }
                document.setDocId(docId);
                document.setDocUri(docUri);
                document.setAadharNumber(uploadDocRequest.getAadharNumber());
                document.setDocName(file.getOriginalFilename());
                document.setDocType(uploadDocRequest.getDocType());
                document.setDocExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
                document.setDocPath(docPath);
                return digiLockerDocumentRepository.save(document);
            }
        } catch (Exception e) {
            throw new BadDataException("error occured while saving document", e);
        }
        return null;
    }
}
