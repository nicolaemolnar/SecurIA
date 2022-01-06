/*
Created: 26/11/2021
Modified: 26/11/2021
Model: PostgreSQL 11
Database: PostgreSQL 11
*/

-- Table Client ---------------------------------------------------------------
INSERT INTO public.user(
	email, password, first_name, surname, phone_number, birth_date)
	VALUES ('nicolae.molnar@edu.uah.es', '1234', 'Alexandru', 'Molnar', '666333222', '04/03/2001');
	
INSERT INTO public.user(
	email, password, first_name, surname, phone_number, birth_date)
	VALUES ('alvaro.barragan@edu.uah.es', '1234', 'Álvaro', 'Barragán', '262153627', '11/11/2001');
	
INSERT INTO public.user(
	email, password, first_name, surname, phone_number, birth_date)
	VALUES ('alberto.estevez@edu.uah.es', '1234', 'Alberto', 'Estévez', '242235562', '12/06/2001');
	
INSERT INTO public.user(
	email, password, first_name, surname, phone_number, birth_date)
	VALUES ('eduardo.garzo@edu.uah.es', '1234', 'Eduardo', 'Garzo', '997228334', '14/09/2001');

INSERT INTO public.user(
	email, password, first_name, surname, phone_number, birth_date)
	VALUES ('admin@securia.com','admin','admin','','',null);

-- Table System ---------------------------------------------------------------
INSERT INTO public.system(
	system_id, capture_photos, capture_videos, live_streaming)
	VALUES ('1', 'True', 'True', 'True');
	
INSERT INTO public.system(
	system_id, capture_photos, capture_videos, live_streaming)
	VALUES ('2', 'True', 'True', 'True');
	
INSERT INTO public.system(
	system_id, capture_photos, capture_videos, live_streaming)
	VALUES ('3', 'True', 'False', 'True');
	
INSERT INTO public.system(
	system_id, capture_photos, capture_videos, live_streaming)
	VALUES ('4', 'True', 'True', 'True');

-- Table Configurations --------------------------------------------------------
INSERT INTO public.configurations(
	email, system_id)
	VALUES ('nicolae.molnar@edu.uah.es', '1');

INSERT INTO public.configurations(
    email, system_id)
    VALUES ('alvaro.barragan@edu.uah.es', '2');

INSERT INTO public.configurations(
    email, system_id)
    VALUES ('alberto.estevez@edu.uah.es', '3');

INSERT INTO public.configurations(
    email, system_id)
    VALUES ('eduardo.garzo@edu.uah.es', '4');

-- Table Alert ---------------------------------------------------------------
INSERT INTO public.alert(
	id, title, description)
	VALUES ('1', 'Movement detected', 'Movement detected in the room 1');

INSERT INTO public.alert(
    id, title, description)
    VALUES ('2', 'Movement detected', 'Movement detected in the room 2');

INSERT INTO public.alert(
    id, title, description)
    VALUES ('3', 'Body detected', 'Body detected at the door');

INSERT INTO public.alert(
    id, title, description)
    VALUES ('4', 'Person detected', 'Person detected at the door');

INSERT INTO public.alert(
    id, title, description)
    VALUES ('5', 'Insufficient light', 'Light sensor detects a low light source');

INSERT INTO public.alert(
    id, title, description)
    VALUES ('6', 'Camera covered', 'Detected low light source while a body is close to the door');

-- Table allow_notifications ---------------------------------------------------
INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('1', '1', 'True');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('1', '2', 'False');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('1', '3', 'True');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('1', '4', 'True');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('1', '5', 'True');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('1', '6', 'True');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('2', '1', 'True');

INSERT INTO public.allow_notifications(
    system_id, alert_id, enabled)
    VALUES ('2', '2', 'False');

-- Table Camera ---------------------------------------------------------------
INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('1', 'Main door', 'True', '1');

INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('2', 'Main door', 'False', '2');

INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('3', 'Main door', 'False', '3');

INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('4', 'Main door', 'False', '4');

-- Table camera_event ----------------------------------------------------------
INSERT INTO public.camera_event(
    id, camera_id, system_id)
    VALUES ('1', '1', '1');


-- Table Sensor_type ----------------------------------------------------------
INSERT INTO public.sensor_type(
    type, name, description, units, min_value, max_value, alert_value, enabled)
    VALUES ('1', 'Light Sensor', 'LDR Sensor', '', '0', '1024', '300', 'True');

INSERT INTO public.sensor_type(
    type, name, description, units, min_value, max_value, alert_value, enabled)
    VALUES ('2', 'Proximity Sensor', 'UltraSonic Distance Sensor', 'cm', '0', '1024', '100', 'True');

INSERT INTO public.sensor_type(
    type, name, description, units, min_value, max_value, alert_value, enabled)
    VALUES ('3', 'Movement Sensor', 'Infrared Sensor', '', '0', '1', '1', 'True');

-- Table sensor_event ---------------------------------------------------------
INSERT INTO public.sensor_event(
    type, id)
    VALUES ('1', '3');