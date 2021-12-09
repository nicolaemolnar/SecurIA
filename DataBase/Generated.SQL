/*
Created: 26/11/2021
Modified: 26/11/2021
Model: PostgreSQL 11
Database: PostgreSQL 11
*/

-- Create tables section -------------------------------------------------

-- Table User

CREATE TABLE "User"
(
  "email" Character varying NOT NULL,
  "password" Character varying NOT NULL,
  "first_name" Character varying NOT NULL,
  "surname" Character varying,
  "phone_number" Character varying,
  "birth_date" Date
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "User" ADD CONSTRAINT "PK_User" PRIMARY KEY ("email")
;

-- Table System

CREATE TABLE "System"
(
  "system_id" Bigint NOT NULL,
  "capture_fotos" Boolean NOT NULL,
  "capture_videos" Boolean NOT NULL,
  "live_streaming" Boolean NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "System" ADD CONSTRAINT "PK_System" PRIMARY KEY ("system_id")
;

-- Table Configurations

CREATE TABLE "Configurations"
(
  "email" Character varying(20) NOT NULL,
  "system_id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "Configurations" ADD CONSTRAINT "PK_Configurations" PRIMARY KEY ("email","system_id")
;

-- Table Alert

CREATE TABLE "Alert"
(
  "id" Bigint NOT NULL,
  "title" Character varying NOT NULL,
  "description" Character varying
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "Alert" ADD CONSTRAINT "PK_Alert" PRIMARY KEY ("id")
;

-- Table Sensor_type

CREATE TABLE "Sensor_type"
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

ALTER TABLE "Sensor_type" ADD CONSTRAINT "PK_Sensor_type" PRIMARY KEY ("type")
;

-- Table Camera

CREATE TABLE "Camera"
(
  "camera_id" Bigint NOT NULL,
  "name" Character varying NOT NULL,
  "detection" Boolean NOT NULL,
  "system_id" Bigint NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "Camera" ADD CONSTRAINT "PK_Camera" PRIMARY KEY ("camera_id","system_id")
;

-- Table camera_event

CREATE TABLE "camera_event"
(
  "id" Bigint NOT NULL,
  "camera_id" Bigint NOT NULL,
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
  "id" Bigint NOT NULL,
  "enabled" Boolean NOT NULL
)
WITH (
  autovacuum_enabled=true)
;

ALTER TABLE "allow_notifications" ADD CONSTRAINT "PK_allow_notifications" PRIMARY KEY ("system_id","id")
;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE "Configurations"
  ADD CONSTRAINT "user_conf"
    FOREIGN KEY ("email")
    REFERENCES "User" ("email")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "Configurations"
  ADD CONSTRAINT "system_conf"
    FOREIGN KEY ("system_id")
    REFERENCES "System" ("system_id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "Camera"
  ADD CONSTRAINT "hasCamera"
    FOREIGN KEY ("system_id")
    REFERENCES "System" ("system_id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "camera_event"
  ADD CONSTRAINT "is_alerted"
    FOREIGN KEY ("id")
    REFERENCES "Alert" ("id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "camera_event"
  ADD CONSTRAINT "alerts"
    FOREIGN KEY ("camera_id", "system_id")
    REFERENCES "Camera" ("camera_id", "system_id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "sensor_event"
  ADD CONSTRAINT "alerts"
    FOREIGN KEY ("type")
    REFERENCES "Sensor_type" ("type")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "sensor_event"
  ADD CONSTRAINT "is_alerted"
    FOREIGN KEY ("id")
    REFERENCES "Alert" ("id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "allow_notifications"
  ADD CONSTRAINT "recieves"
    FOREIGN KEY ("system_id")
    REFERENCES "System" ("system_id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

ALTER TABLE "allow_notifications"
  ADD CONSTRAINT "notificates"
    FOREIGN KEY ("id")
    REFERENCES "Alert" ("id")
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
;

