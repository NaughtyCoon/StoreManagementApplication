CREATE TABLE IF NOT EXISTS suppliers
(
    ID         UUID PRIMARY KEY,
    name       TEXT NOT NULL CHECK (TRIM(name) <> ''),
    email      TEXT NOT NULL CHECK (TRIM(name) <> '') UNIQUE,
    phone      TEXT,
    address    TEXT,
    webSite    TEXT,
    updated_At TIMESTAMP
);
