#!/usr/bin/env bash
set -euo pipefail
umask 077
cd ~/practicas/url-shortener
FILE=url_db-$(date +%F_%H%M)
FOLDER=~/practicas/backups/url-shortener
trap 'rm -f "$FOLDER/$FILE.sql.gz.tmp"' EXIT
docker compose exec -T db sh -c 'pg_dump --no-owner -U "$POSTGRES_USER" -d url_db' | gzip > "$FOLDER/$FILE.sql.gz.tmp"
gzip -t "$FOLDER/$FILE.sql.gz.tmp" 
[ -s "$FOLDER/$FILE.sql.gz.tmp" ]
mv "$FOLDER/$FILE.sql.gz.tmp" "$FOLDER/$FILE.sql.gz"
find  "$FOLDER" -name 'url_db-*.sql.gz' -mtime +7 -delete