CREATE USER pr_user WITH ENCRYPTED PASSWORD 'pr_user_pass';
CREATE SCHEMA IF NOT EXISTS pos AUTHORIZATION pr_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pos TO pr_user;
GRANT USAGE,CREATE ON SCHEMA pos TO pr_user;