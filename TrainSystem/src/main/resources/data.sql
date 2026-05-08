INSERT INTO station (name) VALUES ('Bucuresti Nord');   -- ID 1
INSERT INTO station (name) VALUES ('Ploiesti Vest');   -- ID 2
INSERT INTO station (name) VALUES ('Brasov');           -- ID 3
INSERT INTO station (name) VALUES ('Cluj-Napoca');      -- ID 4
INSERT INTO station (name) VALUES ('Vatra Dornei');     -- ID 5
INSERT INTO station (name) VALUES ('Suceava');          -- ID 6
INSERT INTO station (name) VALUES ('Pascani');          -- ID 7

INSERT INTO train (train_number, total_seats) VALUES ('IR1521', 100);
INSERT INTO train (train_number, total_seats) VALUES ('IR1833', 80);

INSERT INTO route (name, train_id) VALUES ('Bucuresti - Brasov', 1);
INSERT INTO route (name, train_id) VALUES ('Cluj - Pascani', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order) VALUES (1, 1, '08:00:00', '08:10:00', 1);
INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order) VALUES (1, 2, '09:00:00', '09:05:00', 2);
INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order) VALUES (1, 3, '10:30:00', '10:40:00', 3);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 4, '06:00:00', '06:15:00', 1);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 5, '09:30:00', '09:40:00', 2);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 6, '11:00:00', '11:15:00', 3);

INSERT INTO route_stop (route_id, station_id, arrival_time, departure_time, stop_order)
VALUES (2, 7, '12:00:00', '12:10:00', 4);