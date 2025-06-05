-- Create databases for all microservices
CREATE DATABASE book_db;
CREATE DATABASE keycloakdb;

-- Grant all privileges to test user for all databases
GRANT ALL PRIVILEGES ON DATABASE book_db TO test;
GRANT ALL PRIVILEGES ON DATABASE keycloakdb TO test;

-- Connect to each database and grant schema permissions
\c book_db;
GRANT ALL ON SCHEMA public TO test;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO test;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO test;

\c keycloakdb;
GRANT ALL ON SCHEMA public TO test;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO test;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO test;