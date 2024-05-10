CREATE TYPE id_document_type AS ENUM ('ID_CARD', 'PASSPORT', 'DRIVING_LICENSE');

CREATE TABLE IF NOT EXISTS id_document (
    id                        uuid not null default gen_random_uuid(),
    username                  varchar(255) not null unique,
    email                     varchar(254) not null,
    fiscal_code               varchar(16) not null unique,
    id_document_number        varchar(10) not null unique,
    id_document_type          id_document_type not null,
    id_document_issuing_date  date not null,
    id_document_expiring_date date not null,
    id_document_url           varchar(254) not null,
    insert_datetime           timestamp not null default now(),
    update_datetime           timestamp,
    primary key (id)
);
