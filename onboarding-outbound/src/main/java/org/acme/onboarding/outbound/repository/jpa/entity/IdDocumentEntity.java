package org.acme.onboarding.outbound.repository.jpa.entity;

import jakarta.persistence.*;
import org.acme.onboarding.domain.model.id_document.IdDocumentType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.acme.onboarding.outbound.repository.jpa.entity.IdDocumentEntity.FIND_BY_USERNAME;

@Entity
@Table(name = "id_document", schema = "onboarding")
@NamedQuery(
        name = FIND_BY_USERNAME,
        query = "select iddoc from IdDocumentEntity iddoc where iddoc.username =:" + IdDocumentEntity.USERNAME)
public class IdDocumentEntity {
    public static final String FIND_BY_USERNAME = "FIND_BY_USERNAME";
    public static final String USERNAME = "USERNAME";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "fiscal_code", nullable = false, length = 16)
    private String fiscalCode;

    @Column(name = "id_document_number", nullable = false, length = 10)
    private String idDocumentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_document_type", columnDefinition = "id_document_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private IdDocumentType idDocumentType;

    @Column(name = "id_document_issuing_date", nullable = false)
    private LocalDate idDocumentIssuingDate;

    @Column(name = "id_document_expiring_date", nullable = false)
    private LocalDate idDocumentExpiringDate;

    @Column(name = "id_document_url", nullable = false, length = 254)
    private String idDocumentUrl;

    @Column(name = "insert_datetime", nullable = false)
    private Instant insertDatetime;

    @Column(name = "update_datetime")
    private Instant updateDatetime;

    public IdDocumentEntity() {}

    public IdDocumentEntity(
            String username,
            String email,
            String fiscalCode,
            String idDocumentNumber,
            IdDocumentType documentType,
            LocalDate idDocumentIssuingDate,
            LocalDate idDocumentExpiringDate,
            String idDocumentUrl) {
        this.username = username;
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.idDocumentNumber = idDocumentNumber;
        this.idDocumentType = documentType;
        this.idDocumentIssuingDate = idDocumentIssuingDate;
        this.idDocumentExpiringDate = idDocumentExpiringDate;
        this.idDocumentUrl = idDocumentUrl;
        this.insertDatetime = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getIdDocumentNumber() {
        return idDocumentNumber;
    }

    public void setIdDocumentNumber(String idDocumentNumber) {
        this.idDocumentNumber = idDocumentNumber;
    }

    public LocalDate getIdDocumentIssuingDate() {
        return idDocumentIssuingDate;
    }

    public void setIdDocumentIssuingDate(LocalDate idDocumentIssuingDate) {
        this.idDocumentIssuingDate = idDocumentIssuingDate;
    }

    public LocalDate getIdDocumentExpiringDate() {
        return idDocumentExpiringDate;
    }

    public void setIdDocumentExpiringDate(LocalDate idDocumentExpiringDate) {
        this.idDocumentExpiringDate = idDocumentExpiringDate;
    }

    public String getIdDocumentUrl() {
        return idDocumentUrl;
    }

    public void setIdDocumentUrl(String idDocumentUrl) {
        this.idDocumentUrl = idDocumentUrl;
    }

    public Instant getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(Instant insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    public Instant getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Instant updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public IdDocumentType getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(IdDocumentType idDocumentType) {
        this.idDocumentType = idDocumentType;
    }
}
