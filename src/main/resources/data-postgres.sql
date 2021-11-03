/*USERS*/
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Goca', 'Petrusic', 'goca@gmail.com', 'goca', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'System_administrator', true, 0);

INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Vikendica', 'Petrusic', 'vikendica@gmail.com', 'vikendica', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Cottage_owner', true, 1);

INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Brod', 'Petrusic', 'brod@gmail.com', 'brod', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Boat_owner', true, 2);

INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Klijent', 'Petrusic', 'klijent@gmail.com', 'klijent', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Client', true, 3);

INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS)
VALUES('Vikendica 1', 'Srbija', 'Novi Sad', 'Ledinci', 3, 5);