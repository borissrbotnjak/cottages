/* LOYALTY PROGRAM*/
INSERT INTO loyalty_program(appointment_points, consultation_points, discount_gold, discount_regular, discount_silver, gold_points, regular_points, silver_points)
VALUES(0,0,0,0,0,1,5,3);
/*
INSERT INTO USERS_LOYALTY_PROGRAM(SYSTEM_ADMINISTRATOR_ID, LOYALTY_PROGRAMS_ID)
VALUES(1,1);
*/

/* USERS */
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Goca', 'Petrusic', 'goca@gmail.com', 'goca', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'System_administrator', TRUE, 0);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Vikendica', 'Petrusic', 'v@gmail.com', 'v', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Cottage_owner', TRUE, 1);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Vikendica2', 'Petrusic2', 'v2@gmail.com', 'v2', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Cottage_owner', TRUE, 1);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Brod', 'Petrusic', 'brod@gmail.com', 'brod', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Boat_owner', TRUE, 2);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role, loyalty_program_id)
VALUES ('Klijent', 'Petrusic', 'klijent@gmail.com', 'kl', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Client', TRUE, 3, 1);

/* BOATS */
INSERT INTO BOAT(boat_name, boat_owner_id)
VALUES('Dalila', 4);
INSERT INTO BOAT(boat_name, boat_owner_id)
VALUES('Zaklina', 4);

/* COTTAGES */
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED)
VALUES('Vikendica 1', 'Srbija', 'Novi Sad', 'Ledinci', 3, 5, 4.1, '', 2, TRUE);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED)
VALUES('Vikendica 2', 'Srbija', 'Novi Sad', 'Veternik', 1, 3, 3.4, '', 2, FALSE);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED)
VALUES('Vikendica 3', 'Srbija', 'Palic', 'Palic', 3, 2, 4.0, '', 3, TRUE);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED)
VALUES('Vikendica 4', 'Srbija', 'Novi Sad', 'Vrdnik', 4, 2, 4.5, '', 2, FALSE);

/* ADVENTURES */
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(adventure_name, adventure_residence, adventure_city, adventure_state, adventure_description, instructor_info, average_rating)
VALUES ('Avantura 1', 'Zlatar', 'Nova Varos', 'Srbija', 'Opis avanture 1', 'Milorad Otasevic', 4.2);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(adventure_name, adventure_residence, adventure_city, adventure_state,
                                         adventure_description, instructor_info, average_rating)
VALUES ('Avantura 2', 'Palic', 'Palic', 'Srbija', 'Opis', 'Istvan Tamas', 3.9);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(INSTRUCTOR_INFO, ADVENTURE_NAME, ADVENTURE_RESIDENCE, ADVENTURE_CITY,
                                         ADVENTURE_STATE, AVERAGE_RATING)
VALUES ('Predrag Maric', 'Pecanje na Dunavu', 'Smederevo', 'Smederevo', 'Srbija', 4.2);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(INSTRUCTOR_INFO, ADVENTURE_NAME, ADVENTURE_RESIDENCE, ADVENTURE_CITY,
                                         ADVENTURE_STATE, AVERAGE_RATING)
VALUES ('Maja Simovic', 'Pecanje na Zapadnoj Moravi', 'Trstenik', 'Trstenik', 'Srbija', 4.4);

/* COTTAGE_RESERVATIONS-DISCOUNTS */
INSERT INTO reservation(COTTAGE_ID, CLIENT_ID, COTTAGE_OWNER_ID, DISCOUNT_AVAILABLE_FROM, DISCOUNT_AVAILABLE_UNTIL,
                        DISCOUNT, NUM_PERSONS, PRICE, RESERVATION_TYPE, START_TIME, END_TIME)
VALUES (1, 5, 2, '4/11/2021 10:00', '8/11/2021 10:00', TRUE, 4, 1000, 'cottage_reservation', '5/11/2022 12:00',
        '7/11/2022 12:00');

/* COTTAGE_RESERVATIONS-PAST */
INSERT INTO reservation(COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (1, 2, 5, FALSE, TRUE, '4/11/2021 12:00', '5/11/2021 12:00', 'cottage_reservation');
INSERT INTO reservation(COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (3, 3, 5, FALSE, TRUE, '4/11/2021 12:00', '5/11/2021 12:00', 'cottage_reservation');

/* COTTAGE_RESERVATIONS-UPCOMING */
INSERT INTO reservation(COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (1, 2, 5, FALSE, TRUE, '5/11/2022 12:00', '6/11/2022 12:00', 'cottage_reservation');

/* BOAT RESERVATIONS */
INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 5, FALSE, TRUE, '4/11/2021 12:00', '8/11/2021 12:00', FALSE, 'boat_reservation');


INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 5, FALSE, TRUE, '4/11/2022 12:00', '8/11/2022 12:00', FALSE, 'boat_reservation');
INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (2, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'boat_reservation');

/* INSTRUCTOR RESERVATIONS */
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 5, FALSE, TRUE, '4/11/2021 12:00', '8/11/2021 12:00', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (4, 5, FALSE, TRUE, '4/11/2021 12:00', '8/11/2021 12:00', FALSE, 'instructor_reservation');


INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 5, FALSE, TRUE, '4/11/2022 12:00', '8/11/2022 12:00', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (2, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (3, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'instructor_reservation');