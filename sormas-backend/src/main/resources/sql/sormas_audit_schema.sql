-- If a DB update was performed, insert a new line with a comment to the table SCHEMA_VERSION.
-- Example: INSERT INTO schema_version (version_number, comment) VALUES (1, 'Init database');

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;

CREATE TABLE schema_version (
  version_number integer NOT NULL,
  changedate timestamp without time zone NOT NULL DEFAULT now(),
  comment character varying(255),
  CONSTRAINT schema_version_pkey PRIMARY KEY (version_number)
);

ALTER TABLE schema_version OWNER TO sormas_user;

INSERT INTO schema_version (version_number, comment) VALUES (1, 'Basic database configuration');

CREATE SEQUENCE auditlog_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE auditlog_seq OWNER TO sormas_user;

CREATE TABLE auditlogentry (
  id bigint NOT NULL,
  detection_ts timestamp without time zone NOT NULL,
  changetype character varying(255) NOT NULL,
  editinguser character varying(255),
  clazz character varying(255),
  uuid character varying(255),
  transaction_id character varying(255),
  transaction_ts timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT auditlogentry_pkey PRIMARY KEY (id)
);
ALTER TABLE auditlogentry OWNER TO sormas_user;

CREATE TABLE auditlogentry_attributes (
  auditlogentry_id bigint NOT NULL,
  attribute_key character varying(255) NOT NULL,
  attribute_value text,
  CONSTRAINT fk_attribute_changes_auditlogentry_id FOREIGN KEY (auditlogentry_id) REFERENCES auditlogentry (id)
);
ALTER TABLE auditlogentry_attributes OWNER TO sormas_user;

INSERT INTO schema_version (version_number, comment) VALUES (2, 'Initial entity model');

--	March 04, 2021 use User.id instead of user.username
--	first, copy users table from sormas_db to sormas_audit_db
--		cd "/c/Program Files/PostgreSQL/10/bin/"
--		.\pg_dump -U sormas_user -t users sormas_db | .\psql -U sormas_user sormas_audit_db;
--	then, set editinguser.id from the users table using the username field

ALTER TABLE auditlogentry ADD editinguserid BIGINT NULL;

UPDATE auditlogentry
SET editinguserid = u.id
FROM auditlogentry e
LEFT JOIN users u ON e.editinguser = u.username
WHERE e.id = auditlogentry.id;

--	we no longer need users table
DROP TABLE users;

INSERT INTO schema_version (version_number, comment) VALUES (3, 'Use user.id instead of user.username');
