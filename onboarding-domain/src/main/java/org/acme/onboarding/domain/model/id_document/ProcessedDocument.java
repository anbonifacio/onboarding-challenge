package org.acme.onboarding.domain.model.id_document;

import java.time.LocalDate;

public record ProcessedDocument(
        FiscalCode fiscalCode,
        IdDocumentNumber idDocumentNumber,
        IdDocumentType idDocumentType,
        LocalDate issuingDate,
        LocalDate expiringDate,
        IdDocumentUrl documentUrl) {

    public static final class Builder {
        private FiscalCode fiscalCode;
        private IdDocumentNumber idDocumentNumber;
        private IdDocumentType idDocumentType;
        private LocalDate issuingDate;
        private LocalDate expiringDate;
        private IdDocumentUrl documentUrl;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder withFiscalCode(FiscalCode fiscalCode) {
            this.fiscalCode = fiscalCode;
            return this;
        }

        public Builder withIdDocumentNumber(IdDocumentNumber idDocumentNumber) {
            this.idDocumentNumber = idDocumentNumber;
            return this;
        }

        public Builder withIdDocumentType(IdDocumentType idDocumentType) {
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

        public Builder withDocumentUrl(IdDocumentUrl documentUrl) {
            this.documentUrl = documentUrl;
            return this;
        }

        public ProcessedDocument build() {
            return new ProcessedDocument(
                    fiscalCode, idDocumentNumber, idDocumentType, issuingDate, expiringDate, documentUrl);
        }
    }
}
