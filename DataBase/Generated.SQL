/*
Created: 26/11/2021
Modified: 13/01/2022
Model: PostgreSQL 11
Database: PostgreSQL 11
*/

-- Create tables section -------------------------------------------------

-- Table user

CREATE TABLE "user"
(
  "email" Character varying(100) NOT NULL,
  "password" Character varying NOT NULL,
  "first_name" Character varying NOT NULL,
  "surname" Character varying NOT NULL,
  "phone_number" Character varying NOT NULL,
  "birth_date" Date
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "user" ADD CONSTRAINT "PK_user" PRIMARY KEY ("email")
;

-- Table system

CREATE TABLE "system"
(
  "system_id" Bigint NOT NULL,
  "capture_photos" Boolean NOT NULL,
  "send_notifications" Boolean NOT NULL,
  "live_streaming" Boolean NOT NULL,
  "email" Character varying(100) NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

CREATE INDEX "IX_Relationship3" ON "system" ("email")
;

ALTER TABLE "system" ADD CONSTRAINT "PK_system" PRIMARY KEY ("system_id")
;

-- Table alert

CREATE TABLE "alert"
(
  "id" Bigint NOT NULL,
  "title" Character varying NOT NULL,
  "description" Character varying,
  "system_id" Bigint NOT NULL,
  "timestamp" Timestamp NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

CREATE INDEX "IX_Relationship1" ON "alert" ("system_id")
;

ALTER TABLE "alert" ADD CONSTRAINT "PK_alert" PRIMARY KEY ("id")
;

-- Table camera

CREATE TABLE "camera"
(
  "camera_id" Character varying NOT NULL,
  "name" Character varying NOT NULL,
  "detection" Character varying NOT NULL,
  "system_id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "camera" ADD CONSTRAINT "PK_camera" PRIMARY KEY ("camera_id","system_id")
;

-- Table camera_event

CREATE TABLE "camera_event"
(
  "id" Bigint NOT NULL,
  "camera_id" Character varying NOT NULL,
  "system_id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "camera_event" ADD CONSTRAINT "PK_camera_event" PRIMARY KEY ("id","camera_id","system_id")
;

-- Table sensor_event

CREATE TABLE "sensor_event"
(
  "id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "sensor_event" ADD CONSTRAINT "PK_sensor_event" PRIMARY KEY ("id")
;

-- Table contact_request

CREATE TABLE "contact_request"
(
  "request_id" Bigint NOT NULL,
  "name" Character varying NOT NULL,
  "email" Character varying(100) NOT NULL,
  "phone_number" Character varying,
  "company_name" Character varying,
  "message" Text NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "contact_request" ADD CONSTRAINT "PK_contact_request" PRIMARY KEY ("request_id")
;

-- Table image

CREATE TABLE "image"
(
  "path" Text NOT NULL,
  "timestamp" Timestamp NOT NULL,
  "label" Text NOT NULL,
  "camera_id" Character varying NOT NULL,
  "system_id" Bigint NOT NULL,
  "image_id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "image" ADD CONSTRAINT "PK_image" PRIMARY KEY ("camera_id","system_id","image_id")
;

-- Table sensor_type

CREATE TABLE "sensor_type"
(
  "id" Bigint NOT NULL,
  "type" Character varying NOT NULL,
  "unique_per_system" Boolean NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "sensor_type" ADD CONSTRAINT "PK_sensor_type" PRIMARY KEY ("id")
;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE "camera"
  ADD CONSTRAINT "hasCamera"
    FOREIGN KEY ("system_id")
    REFERENCES "system" ("system_id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "camera_event"
  ADD CONSTRAINT "is_alerted"
    FOREIGN KEY ("id")
    REFERENCES "alert" ("id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "camera_event"
  ADD CONSTRAINT "alerts"
    FOREIGN KEY ("camera_id", "system_id")
    REFERENCES "camera" ("camera_id", "system_id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "sensor_event"
  ADD CONSTRAINT "is_alerted"
    FOREIGN KEY ("id")
    REFERENCES "alert" ("id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "image"
  ADD CONSTRAINT "records"
    FOREIGN KEY ("camera_id", "system_id")
    REFERENCES "camera" ("camera_id", "system_id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "alert"
  ADD CONSTRAINT "recieves"
    FOREIGN KEY ("system_id")
    REFERENCES "system" ("system_id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "system"
  ADD CONSTRAINT "has_system"
    FOREIGN KEY ("email")
    REFERENCES "user" ("email")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

