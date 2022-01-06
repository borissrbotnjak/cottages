/* LOYALTY PROGRAM*/
INSERT INTO loyalty_program(appointment_points, consultation_points, discount_gold, discount_regular, discount_silver, gold_points, regular_points,
                            silver_points, penalties)
VALUES(0,0,0,0,0,1,5,3,0);
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
VALUES ('Brod', 'Petrusic', 'b@gmail.com', 'b', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Boat_owner', TRUE, 2);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role, loyalty_program_id)
VALUES ('Klijent', 'Petrusic', 'klijent@gmail.com', 'kl', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Client', TRUE, 3, 1);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role, loyalty_program_id)
VALUES ('Jovan', 'Memedovic', 'jovanm@gmail.com', 'kl', 'Novi Sad', 'Novi Sad', 'Srbija', '0641234567', 'Client', TRUE, 3, 1);
INSERT INTO users(first_name, last_name, email, password, residence, city, state, phone_number, user_type, enabled, user_role)
VALUES ('Brod2', 'Brodic', 'b2@gmail.com', 'brod', 'Beograd', 'Beograd', 'Srbija', '0641234567', 'Boat_owner', TRUE, 2);

/* BOATS */
INSERT INTO BOAT(boat_name, engine_type, length, engine_number, engine_power, max_speed, capacity, rules, description, boat_owner_id, average_rating, deleted, reserved, available_from, available_until, cancellation_condition, image_url, SUBSCRIBER_ID)
VALUES('Dalila', 0, 5, 2, 250, 35, 8, '', '', 4, 4.7, false, false, '08/08/2021 12:00', '01/01/2022 12:00', 0, 'https://i.pinimg.com/564x/c7/95/9f/c7959f829431e3616dcfa2ec9c48e110.jpg', 5);
INSERT INTO BOAT(boat_name, engine_type, length, engine_number, engine_power, max_speed, capacity, rules, description, boat_owner_id, average_rating, deleted, reserved, available_from, available_until, cancellation_condition, image_url, SUBSCRIBER_ID)
VALUES('Zaklina', 0, 7, 2, 200, 40, 8, '', '', 4, 4.1, false, false, '08/08/2021 12:00', '01/01/2022 12:00', 0, 'http://inverlochmarine.com.au/sites/default/files/styles/large/public/2_5.jpg?itok=Zo3bRpNi', 5);
INSERT INTO BOAT(boat_name, engine_type, length, engine_number, engine_power, max_speed, capacity, rules, description, boat_owner_id, average_rating, deleted, reserved, available_from, available_until, cancellation_condition, image_url, SUBSCRIBER_ID)
VALUES('Bojana', 1, 7, 2, 300, 40, 8, '', '', 4, 4.1, false, true, '08/08/2021 12:00', '01/01/2022 12:00', 1, 'https://i.pinimg.com/564x/b4/27/94/b42794f63240d0f7aa3454ef85eb168c.jpg', 6);
INSERT INTO BOAT(boat_name, engine_type, length, engine_number, engine_power, max_speed, capacity, rules, description, boat_owner_id, average_rating, deleted, reserved, cancellation_condition, image_url)
VALUES('Kristina', 3, 7, 2, 200, 40, 8, '', '', 6, 4.6, false, true, 1, 'https://i.pinimg.com/564x/e1/5b/4a/e15b4ad8938cf85f98b45e0b83dd701b.jpg');

/* COTTAGES */
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, AVAILABLE_FROM,
                    AVAILABLE_UNTIL, IMAGE_URL, SUBSCRIBER_ID)
VALUES('Vikendica 1', 'Srbija', 'Novi Sad', 'Ledinci', 3, 5, 4.1, '', 2, TRUE, '08/08/2021 12:00', '01/01/2022 12:00', 'https://i.pinimg.com/564x/0e/08/ab/0e08ab8052d8176950710801b798cce1.jpg', 5);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, IMAGE_URL
, SUBSCRIBER_ID)
VALUES('Vikendica 2', 'Srbija', 'Novi Sad', 'Veternik', 1, 3, 3.4, '', 2, FALSE, 'https://tinyhousetalk.com/wp-content/uploads/Pendleton-Cottage-001.jpg', 5);
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, AVAILABLE_FROM,
                    AVAILABLE_UNTIL, IMAGE_URL)
VALUES('Vikendica 3', 'Srbija', 'Palic', 'Palic', 3, 2, 4.0, '', 3, TRUE, '08/08/2021 12:00', '01/01/2022 12:00', '');
INSERT INTO COTTAGE(NAME, STATE, CITY, RESIDENCE, NUMBER_OF_ROOMS, NUMBER_OF_BEDS, AVERAGE_RATING, RULES, COTTAGE_OWNER_ID, RESERVED, IMAGE_URL)
VALUES('Vikendica 4', 'Srbija', 'Novi Sad', 'Vrdnik', 4, 2, 4.5, '', 2, FALSE, 'https://i.pinimg.com/564x/83/97/8c/83978c07ed7eeabc18013e853c618493.jpg');

/* ADDITIONAL SERVICES */
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, COTTAGE_ID)
VALUES('usluga1', 500, 1);
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, COTTAGE_ID)
VALUES('usluga2', 300, 1);
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, COTTAGE_ID)
VALUES('usluga1', 300, 2);
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, COTTAGE_ID)
VALUES('usluga1', 300, 3);
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, COTTAGE_ID)
VALUES('usluga1', 300, 4);

INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, BOAT_ID)
VALUES('usluga1', 300, 3);
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, BOAT_ID)
VALUES('usluga1', 1000, 2);
INSERT INTO ADDITIONAL_SERVICE(NAME, PRICE, BOAT_ID)
VALUES('usluga2', 500, 3);

/* ADVENTURES */
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(adventure_name, adventure_residence, adventure_city, adventure_state, adventure_description,
                                         instructor_info, average_rating, subscriber_id)
VALUES ('Avantura 1', 'Zlatar', 'Nova Varos', 'Srbija', 'Opis avanture 1', 'Milorad Otasevic', 4.2, 5);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(adventure_name, adventure_residence, adventure_city, adventure_state,
                                         adventure_description, instructor_info, average_rating, subscriber_id)
VALUES ('Avantura 2', 'Palic', 'Palic', 'Srbija', 'Opis', 'Istvan Tamas', 3.9, 5);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(INSTRUCTOR_INFO, ADVENTURE_NAME, ADVENTURE_RESIDENCE, ADVENTURE_CITY,
                                         ADVENTURE_STATE, AVERAGE_RATING)
VALUES ('Predrag Maric', 'Pecanje na Dunavu', 'Smederevo', 'Smederevo', 'Srbija', 4.2);
INSERT INTO FISHING_INSTRUCTOR_ADVENTURE(INSTRUCTOR_INFO, ADVENTURE_NAME, ADVENTURE_RESIDENCE, ADVENTURE_CITY,
                                         ADVENTURE_STATE, AVERAGE_RATING)
VALUES ('Maja Simovic', 'Pecanje na Zapadnoj Moravi', 'Trstenik', 'Trstenik', 'Srbija', 4.4);

/* COTTAGE_RESERVATIONS-DISCOUNTS */
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, CLIENT_ID, COTTAGE_OWNER_ID, DISCOUNT_AVAILABLE_FROM, DISCOUNT_AVAILABLE_UNTIL,
                        DISCOUNT, NUM_PERSONS, PRICE, RESERVATION_TYPE, START_TIME, END_TIME)
VALUES (1, 1, 5, 2, '04/11/2021 10:00', '08/11/2021 10:00', TRUE, 4, 1000, 'cottage_reservation', '05/11/2022 12:00', '07/11/2022 12:00');
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, CLIENT_ID, COTTAGE_OWNER_ID, DISCOUNT_AVAILABLE_FROM, DISCOUNT_AVAILABLE_UNTIL,
                        DISCOUNT, NUM_PERSONS, PRICE, RESERVATION_TYPE, START_TIME, END_TIME)
VALUES (1, 1, 6, 2, '09/11/2021 10:00', '12/11/2021 10:00', TRUE, 4, 1000, 'cottage_reservation', '05/11/2022 12:00', '07/11/2022 12:00');

/* COTTAGE_RESERVATIONS-FREE */
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (1, 1, 2, 5, FALSE, FALSE, '06/11/2022 12:00', '12/12/2022 12:00', 'cottage_reservation');
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (1, 1, 2, 5, FALSE, FALSE, '06/05/2022 12:00', '12/12/2022 12:00', 'cottage_reservation');
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (1, 1, 2, 5, FALSE, FALSE, '06/04/2022 12:00', '12/10/2022 12:00', 'cottage_reservation');

/* COTTAGE_RESERVATIONS-PAST */
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 1, 2, 5, FALSE, TRUE, '04/11/2021 12:00', '05/11/2021 12:00', 'cottage_reservation', 1, 3000);
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 3, 5, FALSE, TRUE, '04/06/2021 12:00', '06/06/2021 12:00', 'cottage_reservation', 3, 2400);
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 3, 6, FALSE, TRUE, '04/06/2021 12:00', '06/06/2021 12:00', 'cottage_reservation', 3, 2400);

/* COTTAGE_RESERVATIONS-UPCOMING */
INSERT INTO reservation(ADMIN_ID, COTTAGE_ID, COTTAGE_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME,
                        RESERVATION_TYPE)
VALUES (1, 1, 2, 5, FALSE, TRUE, '05/11/2022 12:00', '06/11/2022 12:00', 'cottage_reservation');

/* BOAT RESERVATIONS */
INSERT INTO reservation(ADMIN_ID, BOAT_ID, BOAT_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 1, 4, 5, FALSE, TRUE, '04/11/2021 12:00', '07/11/2021 12:00', FALSE, 'boat_reservation', 4, 25000);
INSERT INTO reservation(ADMIN_ID, BOAT_ID, BOAT_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 4, 5, FALSE, TRUE, '04/04/2021 12:00', '09/04/2021 12:00', FALSE, 'boat_reservation', 6, 20000);
INSERT INTO reservation(ADMIN_ID, BOAT_ID, BOAT_OWNER_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 4, 6, FALSE, TRUE, '04/04/2021 12:00', '09/04/2021 12:00', FALSE, 'boat_reservation', 6, 20000);

/* BOAT_RESERVATIONS-FREE */
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 1, 5, 4, FALSE, FALSE, '01/11/2022 12:00', '08/11/2022 12:00', TRUE, 'boat_reservation');
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 2, 5, 4, FALSE, FALSE, '04/03/2022 12:00', '08/03/2022 12:00', FALSE, 'boat_reservation');
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 2, 6, 4, FALSE, FALSE, '03/03/2022 12:00', '12/03/2022 12:00', FALSE, 'boat_reservation');

/* BOAT_RESERVATIONS-UPCOMING */
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 1, 5, 4, FALSE, TRUE, '4/11/2022 12:00', '8/11/2022 12:00', FALSE, 'boat_reservation');
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 2, 5, 4, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'boat_reservation');
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 2, 6, 4, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'boat_reservation');

/* BOAT_RESERVATIONS-PAST*/
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 5, 4, FALSE, TRUE, '04/04/2021 12:00', '09/04/2021 12:00', FALSE, 'boat_reservation', 6, 15000);
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 5, 4, FALSE, TRUE, '05/05/2021 12:00', '09/05/2021 12:00', FALSE, 'boat_reservation', 6, 20000);
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 5, 4, FALSE, TRUE, '04/04/2020 12:00', '09/04/2020 12:00', FALSE, 'boat_reservation', 6, 23000);
INSERT INTO reservation(ADMIN_ID, BOAT_ID, CLIENT_ID, BOAT_OWNER_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 3, 6, 4, FALSE, TRUE, '04/04/2020 12:00', '09/04/2020 12:00', FALSE, 'boat_reservation', 6, 23000);

/* BOAT_RESERVATIONS-DISCOUNTS */
INSERT INTO reservation(ADMIN_ID, BOAT_ID, BOAT_OWNER_ID, DISCOUNT_AVAILABLE_FROM, DISCOUNT_AVAILABLE_UNTIL, NUM_PERSONS, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE, DURATION, PRICE)
VALUES (1, 1, 4, '02/12/2021 12:00', '12/12/2021 12:00', 2, 5, FALSE, TRUE, '04/11/2021 12:00', '07/11/2021 12:00', TRUE, 'boat_reservation', 4, 1500);

/* INSTRUCTOR RESERVATIONS */
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        DURATION, PRICE)
VALUES (1, 5, FALSE, TRUE, '4/10/2021 12:00', '7/10/2021 12:00', FALSE, 'instructor_reservation', 4, 6000);
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE,
                        DURATION, PRICE)
VALUES (4, 5, FALSE, TRUE, '4/11/2021 12:00', '8/11/2021 12:00', FALSE, 'instructor_reservation', 5, 7500);


INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (1, 5, FALSE, TRUE, '4/11/2022 12:00', '8/11/2022 12:00', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (2, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'instructor_reservation');
INSERT INTO reservation(INSTRUCTOR_ID, CLIENT_ID, DELETED, RESERVED, START_TIME, END_TIME, DISCOUNT, RESERVATION_TYPE)
VALUES (3, 5, FALSE, TRUE, '4/03/2022 12:00', '8/03/2022 12:00', FALSE, 'instructor_reservation');