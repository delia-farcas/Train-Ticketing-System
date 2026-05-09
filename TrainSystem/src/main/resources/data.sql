-- =========================
-- STATIONS
-- =========================

INSERT INTO station (name) VALUES ('Bucuresti Nord');      -- 1
INSERT INTO station (name) VALUES ('Ploiesti Vest');       -- 2
INSERT INTO station (name) VALUES ('Brasov');              -- 3
INSERT INTO station (name) VALUES ('Cluj-Napoca');         -- 4
INSERT INTO station (name) VALUES ('Vatra Dornei');        -- 5
INSERT INTO station (name) VALUES ('Suceava');             -- 6
INSERT INTO station (name) VALUES ('Pascani');             -- 7
INSERT INTO station (name) VALUES ('Sibiu');               -- 8
INSERT INTO station (name) VALUES ('Sighisoara');          -- 9
INSERT INTO station (name) VALUES ('Targu Mures');         -- 10
INSERT INTO station (name) VALUES ('Iasi');                -- 11
INSERT INTO station (name) VALUES ('Constanta');           -- 12
INSERT INTO station (name) VALUES ('Craiova');             -- 13
INSERT INTO station (name) VALUES ('Timisoara Nord');      -- 14
INSERT INTO station (name) VALUES ('Arad');                -- 15
INSERT INTO station (name) VALUES ('Oradea');              -- 16
INSERT INTO station (name) VALUES ('Alba Iulia');          -- 17
INSERT INTO station (name) VALUES ('Deva');                -- 18
INSERT INTO station (name) VALUES ('Galati');              -- 19
INSERT INTO station (name) VALUES ('Braila');              -- 20

-- =========================
-- TRAINS
-- =========================

INSERT INTO train (train_number, total_seats) VALUES ('IR1521', 100); -- 1
INSERT INTO train (train_number, total_seats) VALUES ('IR1833', 80);  -- 2
INSERT INTO train (train_number, total_seats) VALUES ('IR1745', 120); -- 3
INSERT INTO train (train_number, total_seats) VALUES ('RE3001', 60);  -- 4
INSERT INTO train (train_number, total_seats) VALUES ('IR1602', 90);  -- 5
INSERT INTO train (train_number, total_seats) VALUES ('IC501', 150);  -- 6
INSERT INTO train (train_number, total_seats) VALUES ('RE4502', 70);  -- 7

-- =========================
-- ROUTES
-- =========================

INSERT INTO route (name, train_id) VALUES ('Bucuresti - Brasov', 1);       -- 1
INSERT INTO route (name, train_id) VALUES ('Cluj - Pascani', 2);           -- 2
INSERT INTO route (name, train_id) VALUES ('Bucuresti - Constanta', 3);    -- 3
INSERT INTO route (name, train_id) VALUES ('Timisoara - Oradea', 4);       -- 4
INSERT INTO route (name, train_id) VALUES ('Craiova - Brasov', 5);         -- 5
INSERT INTO route (name, train_id) VALUES ('Iasi - Bucuresti', 6);         -- 6
INSERT INTO route (name, train_id) VALUES ('Arad - Cluj', 7);              -- 7

-- =========================
-- ROUTE 1
-- Bucuresti -> Ploiesti -> Brasov
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (1, 1, NULL, '08:10:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (1, 2, '09:00:00', '09:05:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (1, 3, '10:30:00', NULL, 3);

-- =========================
-- ROUTE 2
-- Cluj -> Vatra Dornei -> Suceava -> Pascani
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 4, NULL, '06:15:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 5, '09:30:00', '09:40:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 6, '11:00:00', '11:15:00', 3);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 7, '12:00:00', NULL, 4);

-- =========================
-- ROUTE 3
-- Bucuresti -> Constanta
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (3, 1, NULL, '07:00:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (3, 20, '09:10:00', '09:15:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (3, 19, '09:45:00', '09:50:00', 3);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (3, 12, '11:30:00', NULL, 4);

-- =========================
-- ROUTE 4
-- Timisoara -> Arad -> Oradea
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (4, 14, NULL, '05:45:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (4, 15, '06:50:00', '07:00:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (4, 16, '09:20:00', NULL, 3);

-- =========================
-- ROUTE 5
-- Craiova -> Sibiu -> Brasov
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (5, 13, NULL, '08:00:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (5, 8, '10:30:00', '10:40:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (5, 3, '13:00:00', NULL, 3);

-- =========================
-- ROUTE 6
-- Iasi -> Pascani -> Bucuresti
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (6, 11, NULL, '06:00:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (6, 7, '07:10:00', '07:20:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (6, 1, '12:00:00', NULL, 3);

-- =========================
-- ROUTE 7
-- Arad -> Deva -> Alba Iulia -> Cluj
-- =========================

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (7, 15, NULL, '09:00:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (7, 18, '10:30:00', '10:40:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (7, 17, '11:50:00', '12:00:00', 3);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (7, 4, '14:30:00', NULL, 4);