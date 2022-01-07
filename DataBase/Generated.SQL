/*
Created: 26/11/2021
Modified: 06/01/2022
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
  "surname" Character varying,
  "phone_number" Character varying,
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
  "capture_videos" Boolean NOT NULL,
  "live_streaming" Boolean NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "system" ADD CONSTRAINT "PK_system" PRIMARY KEY ("system_id")
;

-- Table configurations

CREATE TABLE "configurations"
(
  "email" Character varying(100) NOT NULL,
  "system_id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "configurations" ADD CONSTRAINT "PK_configurations" PRIMARY KEY ("email","system_id")
;

-- Table alert

CREATE TABLE "alert"
(
  "id" Bigint NOT NULL,
  "title" Character varying NOT NULL,
  "description" Character varying
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "alert" ADD CONSTRAINT "PK_alert" PRIMARY KEY ("id")
;

-- Table sensor_type

CREATE TABLE "sensor_type"
(
  "type" Bigint NOT NULL,
  "name" Character varying NOT NULL,
  "description" Character varying,
  "units" Character varying(5) NOT NULL,
  "min_value" Bigint NOT NULL,
  "max_value" Bigint,
  "alert_value" Bigint NOT NULL,
  "enabled" Boolean NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "sensor_type" ADD CONSTRAINT "PK_sensor_type" PRIMARY KEY ("type")
;

-- Table camera

CREATE TABLE "camera"
(
  "camera_id" Character varying NOT NULL,
  "name" Character varying NOT NULL,
  "detection" Boolean NOT NULL,
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
  "type" Bigint NOT NULL,
  "id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "sensor_event" ADD CONSTRAINT "PK_sensor_event" PRIMARY KEY ("type","id")
;

-- Table allow_notifications

CREATE TABLE "allow_notifications"
(
  "system_id" Bigint NOT NULL,
  "alert_id" Bigint NOT NULL,
  "enabled" Boolean NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "allow_notifications" ADD CONSTRAINT "PK_allow_notifications" PRIMARY KEY ("system_id","alert_id")
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

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE "configurations"
  ADD CONSTRAINT "user_conf"
    FOREIGN KEY ("email")
    REFERENCES "user" ("email")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "configurations"
  ADD CONSTRAINT "system_conf"
    FOREIGN KEY ("system_id")
    REFERENCES "system" ("system_id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

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
  ADD CONSTRAINT "alerts"
    FOREIGN KEY ("type")
    REFERENCES "sensor_type" ("type")
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

ALTER TABLE "allow_notifications"
  ADD CONSTRAINT "recieves"
    FOREIGN KEY ("system_id")
    REFERENCES "system" ("system_id")
      ON DELETE CASCADE
      ON UPDATE CASCADE
;

ALTER TABLE "allow_notifications"
  ADD CONSTRAINT "notificates"
    FOREIGN KEY ("alert_id")
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

