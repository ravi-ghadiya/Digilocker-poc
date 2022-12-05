package com.document.issuer.demo.repository;

import com.document.issuer.demo.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigiLockerDocumentRepository extends JpaRepository<Document, Long> {
    Document findByAadharNumber(String aadharNumber);
}
