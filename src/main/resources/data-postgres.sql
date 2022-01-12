/* LOYALTY PROGRAM*/
INSERT INTO loyalty_program(reservation_points, discount_gold, discount_regular, discount_silver, gold_points, regular_points,
                            silver_points, penalties)
VALUES(0,0,0,0,1,5,3,1);
INSERT INTO loyalty_program(reservation_points, discount_gold, discount_regular, discount_silver, gold_points, regular_points,
                            silver_points, penalties)
VALUES(2,0,0,0,1,5,3,0);
INSERT INTO loyalty_program(reservation_points, discount_gold, discount_regular, discount_silver, gold_points, regular_points,
                            silver_points, penalties)
VALUES(2,0,0,0,1,5,3,0);
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
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Brod2', 'Brodic', 'b2@gmail.com', 'brod', 'Beograd', 'Beograd', 'Srbija', '0641234567', 'Boat_owner', TRUE, 2);

INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role, loyalty_program_id)
VALUES ('Klijent', 'Klijent', 'borissrbotnjak@gmail.com', 'kl', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Client', TRUE, 3, 2);

INSERT INTO USERS(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Instruktor', 'Otasevic', 'instruktor@gmail.com', 'in', 'Nova Varos', 'Nova Varos', 'Srbija', '0641234567', 'Instructor', TRUE, 4);
INSERT INTO USERS(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Instruktor', 'Tamas', 'instruktor2@gmail.com', 'in', 'Palic', 'Palic', 'Srbija', '0641234567', 'Instructor', TRUE, 4);
INSERT INTO USERS(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Instruktor', 'Maric', 'instruktor3@gmail.com', 'in', 'Smederevo', 'Smederevo', 'Srbija', '0641234567', 'Instructor', TRUE, 4);
INSERT INTO USERS(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Instruktor', 'Simovic', 'instruktor4@gmail.com', 'in', 'Trstenik', 'Trstenik', 'Srbija', '0641234567', 'Instructor', TRUE, 4);

/* BOATS */
INSERT INTO BOAT(boat_name, boat_owner_id, average_rating, NUM_PERSONS, PRICE)
VALUES('Dalila', 4, 4.7, 5, 4500.0);
INSERT INTO BOAT(boat_name, boat_owner_id, average_rating, num_persons, PRICE)
VALUES('Zaklina', 4, 4.1, 4, 4000.0);
INSERT INTO BOAT(boat_name, boat_owner_id, average_rating, num_persons, PRICE)
VALUES('Kristina', 6, 4.6, 5, 8700.0);
INSERT INTO BOAT(boat_name, boat_owner_id, average_rating, num_persons, PRICE, AVAILABLE_FROM, AVAILABLE_UNTIL)
VALUES('Rebeka', 6, 4.4, 5, 3000.0, '4/03/2022 12:00', '8/03/2022 12:00');

/* COTTAGES */
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, AVAILABLE_FROM,
                    AVAILABLE_UNTIL, NUM_PERSONS, PRICE)
VALUES('Vikendica 1', 'Srbija', 'Novi Sad', 'Ledinci', 3, 5, 4.1, '', 2, TRUE, '08/08/2021 12:00', '01/01/2022 12:00', 5, 10000);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, NUM_PERSONS, PRICE)
VALUES('Vikendica 2', 'Srbija', 'Novi Sad', 'Veternik', 1, 3, 3.4, '', 2, FALSE, 6, 9000);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, AVAILABLE_FROM,
                    AVAILABLE_UNTIL, NUM_PERSONS, PRICE)
VALUES('Vikendica 3', 'Srbija', 'Palic', 'Palic', 3, 2, 4.0, '', 3, TRUE, '08/08/2021 12:00', '01/01/2022 12:00', 3, 7000);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, NUM_PERSONS, PRICE)
VALUES('Vikendica 4', 'Srbija', 'Novi Sad', 'Vrdnik', 4, 2, 4.5, '', 2, FALSE, 7, 15500);

/* ADVENTURES */
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(adventure_name, adventure_residence, adventure_city, adventure_state, adventure_description,
                                         instructor_info, average_rating, available_from, available_until, num_persons, instructor_id, price)
VALUES ('Avantura 1', 'Zlatar', 'Nova Varos', 'Srbija', 'Opis avanture 1', 'Milorad Otasevic', 4.2, '04/11/2021 12:00', '05/11/2021 12:00', 9, 8, 7800);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(adventure_name, adventure_residence, adventure_city, adventure_state, adventure_description,
                                         instructor_info, average_rating, num_persons, instructor_id, price)
VALUES ('Avantura 2', 'Palic', 'Palic', 'Srbija', 'Opis', 'Istvan Tamas', 3.9, 6, 9, 6000);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(INSTRUCTOR_INFO, ADVENTURE_NAME, ADVENTURE_RESIDENCE, ADVENTURE_CITY,
                                         ADVENTURE_STATE, AVERAGE_RATING, available_from, available_until, num_persons, instructor_id, price)
VALUES ('Predrag Maric', 'Pecanje na Dunavu', 'Smederevo', 'Smederevo', 'Srbija', 4.2, '04/11/2021 12:00', '05/11/2021 12:00', 9, 10, 7550);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(INSTRUCTOR_INFO, ADVENTURE_NAME, ADVENTURE_RESIDENCE, ADVENTURE_CITY,
                                         ADVENTURE_STATE, AVERAGE_RATING, num_persons, instructor_id, price)
VALUES ('Maja Simovic', 'Pecanje na Zapadnoj Moravi', 'Trstenik', 'Trstenik', 'Srbija', 4.4, 4, 11, 12000);


/* ADDITIONAL SERVICES */

INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, BOAT_ID) VALUES ('DODATNA USLUGA', 'OPIS', 1500, 3);
INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, BOAT_ID) VALUES ('DODATNA USLUGA', 'OPIS', 1500, 1);
INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, BOAT_ID) VALUES ('DODATNA USLUGA 2', 'OPIS', 2300, 3);

INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, COTTAGE_ID) VALUES ('DODATNA USLUGA', 'OPIS', 1500, 3);
INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, COTTAGE_ID) VALUES ('DODATNA USLUGA', 'OPIS', 1500, 1);
INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, COTTAGE_ID) VALUES ('DODATNA USLUGA 2', 'OPIS', 2300, 3);

INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, ADVENTURE_ID) VALUES ('DODATNA USLUGA', 'OPIS', 1500, 3);
INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, ADVENTURE_ID) VALUES ('DODATNA USLUGA', 'OPIS', 1500, 1);
INSERT INTO ADDITIONAL_SERVICE(NAME, DESCRIPTION, PRICE, ADVENTURE_ID) VALUES ('DODATNA USLUGA 2', 'OPIS', 2300, 3);



/* COTTAGE_RESERVATIONS-DISCOUNTS */
INSERT INTO reservation(COTTAGE_ID, CLIENT_ID, COTTAGE_OWNER_ID, DISCOUNT_AVAILABLE_FROM, DISCOUNT_AVAILABLE_UNTIL,
                        DISCOUNT, NUM_PERSONS, PRICE, RESERVATION_TYPE, START_TIME, END_TIME)
VALUES (1, 5, 2, '04/11/2021 10:00', '08/11/2021 10:00', TRUE, 4, 1000, 'cottage_reservation', '05/11/2022 12:00',
        '07/11/2022 12:00');

/* COTTAGE_RESERVATIONS-PAST */
INSERT INTO reservation(COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 2, 5, FALSE, TRUE, '04/11/2021 12:00', '05/11/2021 12:00', 'cottage_reservation', 1, 3000);
INSERT INTO reservation(COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE, DURATION, PRICE)
VALUES (3, 3, 5, FALSE, TRUE, '04/06/2021 12:00', '06/06/2021 12:00', 'cottage_reservation', 3, 2400);

/* COTTAGE_RESERVATIONS-UPCOMING */
INSERT INTO reservation(COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        START_DATE, END_DATE, RESERVATION_TYPE, NUM_PERSONS, PRICE)
VALUES (1, 2, 5, FALSE, TRUE, '05/11/2022 12:00', '06/11/2022 12:00', '05/11/2022', '06/11/2022', 'cottage_reservation', 5, 10000);

/* BOAT RESERVATIONS */
INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        DURATION, PRICE, NUM_PERSONS)
VALUES (1, 5, FALSE, TRUE, '04/11/2021 12:00', '07/11/2021 12:00', FALSE, 'boat_reservation', 4, 25000, 5);
INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        DURATION, PRICE, NUM_PERSONS)
VALUES (3, 5, FALSE, TRUE, '04/04/2021 12:00', '09/04/2021 12:00', FALSE, 'boat_reservation', 6, 20000, 5);


INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        START_DATE, END_DATE, NUM_PERSONS)
VALUES (1, 5, FALSE, TRUE, '4/11/2022 12:00', '8/11/2022 12:00', FALSE, 'boat_reservation', '4/11/2022', '8/11/2022', 5);
INSERT INTO reservation(BOAT_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        START_DATE, END_DATE, NUM_PERSONS)
VALUES (2, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'boat_reservation', '4/03/2022', '8/03/2022', 4);


/* INSTRUCTOR RESERVATIONS */
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        DURATION, PRICE)
VALUES (1, 5, FALSE, TRUE, '4/10/2021 12:00', '7/10/2021 12:00', FALSE, 'instructor_reservation', 4, 6000);
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        DURATION, PRICE)
VALUES (4, 5, FALSE, TRUE, '4/11/2021 12:00', '8/11/2021 12:00', FALSE, 'instructor_reservation', 5, 7500);


INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, START_DATE, END_DATE, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 5, FALSE, TRUE, '4/11/2022 12:00', '8/11/2022 12:00', '4/11/2022', '8/11/2022', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, START_DATE, END_DATE, DISCOUNT, RESERVATION_TYPE)
VALUES (2, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', '4/03/2022', '8/03/2022', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, START_DATE, END_DATE, DISCOUNT, RESERVATION_TYPE)
VALUES (3, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', '4/03/2022', '8/03/2022', FALSE, 'instructor_reservation');

/* SUBSCRIPTIONS */

INSERT INTO BOAT_SUBSCRIBERS(client_id, boat_id) VALUES(5, 1);
INSERT INTO BOAT_SUBSCRIBERS(client_id, boat_id) VALUES(5, 3);

INSERT INTO COTTAGE_SUBSCRIBERS(client_id, cottage_id) VALUES(5, 1);
INSERT INTO COTTAGE_SUBSCRIBERS(client_id, cottage_id) VALUES(5, 2);

INSERT INTO INSTRUCTOR_SUBSCRIBERS(client_id, instructor_id) VALUES(5, 1);
INSERT INTO INSTRUCTOR_SUBSCRIBERS(client_id, instructor_id) VALUES(5, 3);