CREATE TABLE IF NOT EXISTS stores (
    ID UUID PRIMARY KEY,
    name TEXT NOT NULL,
    location TEXT,
    updated_At TIMESTAMP
);
