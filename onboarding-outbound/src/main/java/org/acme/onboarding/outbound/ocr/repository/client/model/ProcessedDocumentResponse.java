package org.acme.onboarding.outbound.ocr.repository.client.model;

import java.time.LocalDate;

public record ProcessedDocumentResponse(
        String fiscalCode,
        String idDocumentNumber,
        String idDocumentType,
        LocalDate issuingDate,
        LocalDate expiringDate,
        String idDocumentUrl) {

    public static final class Builder {
        private String fiscalCode;
        private String idDocumentNumber;
        private String idDocumentType;
        private LocalDate issuingDate;
        private LocalDate expiringDate;
        private String idDocumentUrl;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder withFiscalCode(String fiscalCode) {
            this.fiscalCode = fiscalCode;
            return this;
        }

        public Builder withIdDocumentNumber(String idDocumentNumber) {
            this.idDocumentNumber = idDocumentNumber;
            return this;
        }

        public Builder withIdDocumentType(String idDocumentType) {
            this.idDocumentType = idDocumentType;
            return this;
        }

        public Builder withIssuingDate(LocalDate issuingDate) {
            this.issuingDate = issuingDate;
            return this;
        }

        public Builder withExpiringDate(LocalDate expiringDate) {
            this.expiringDate = expiringDate;
            return this;
        }

        public Builder withIdDocumentUrl(String idDocumentUrl) {
            this.idDocumentUrl = idDocumentUrl;
            return this;
        }

        public ProcessedDocumentResponse build() {
            return new ProcessedDocumentResponse(
                    fiscalCode, idDocumentNumber, idDocumentType, issuingDate, expiringDate, idDocumentUrl);
        }
    }
}
