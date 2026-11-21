CREATE TABLE IF NOT EXISTS short_urls(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    url_original varchar(2048) NOT NULL,
    short_code varchar(64) NOT NULL UNIQUE,
    created_at timestamp NOT NULL DEFAULT NOW(),
    last_accessed_at timestamp,
    counter int NOT NULL DEFAULT 0
);
CREATE INDEX idx_short_code ON short_urls(short_code);