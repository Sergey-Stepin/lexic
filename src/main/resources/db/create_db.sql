CREATE USER lexic WITH PASSWORD 'lexic';
CREATE DATABASE lexic WITH
    OWNER = lexic
    ENCODING = 'UTF8'
    LC_COLLATE = 'de_DE.utf8'
    LC_CTYPE = 'de_DE.utf8'
    TABLESPACE = pg_default
    TEMPLATE template0
    CONNECTION LIMIT = -1;
