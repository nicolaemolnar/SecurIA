/*
Created: 26/11/2021
Modified: 13/01/2021
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
	system_id, capture_photos, send_notifications, live_streaming, email)
	VALUES ('1', 'True', 'True', 'True', 'nicolae.molnar@edu.uah.es');
	
INSERT INTO public.system(
	system_id, capture_photos, send_notifications, live_streaming, email)
	VALUES ('2', 'True', 'True', 'True', 'alberto.estevez@edu.uah.es');
	
INSERT INTO public.system(
	system_id, capture_photos, send_notifications, live_streaming, email)
	VALUES ('3', 'True', 'False', 'True', 'alvaro.barragan@edu.uah.es');
	
INSERT INTO public.system(
	system_id, capture_photos, send_notifications, live_streaming, email)
	VALUES ('4', 'True', 'True', 'True', 'eduardo.garzo@edu.uah.es');

-- Table Camera ---------------------------------------------------------------
INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('camera_1', 'Main door', 'Undetected', '1');

INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('camera_2', 'Main door', 'Undetected', '2');

INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('camera_3', 'Main door', 'Undetected', '3');

INSERT INTO public.camera(
    camera_id, name, detection, system_id)
    VALUES ('camera_4', 'Main door', 'Undetected', '4');

-- Table Sensor_type ---------------------------------------------------------------
INSERT INTO public.sensor_type(
	id, type, unique_per_system)
	VALUES ('0', 'proximity', 'True');

INSERT INTO public.sensor_type(
	id, type, unique_per_system)
	VALUES ('1', 'movement', 'False');