-- 2
-- -- a
CREATE VIEW IF NOT EXISTS common_names AS
SELECT common_name AS "Common Name", CONCAT(genus_name, " ", species_epithet) AS "Scientific Name"
FROM common_name;

-- -- b
CREATE VIEW IF NOT EXISTS sighting_locations AS
SELECT display_name AS "Display Name", home_latitude AS "Home latitude", home_longitude AS "Home longitude", AVG(latitude) AS "Average latitude", AVG(longitude) AS "Average longitude"
FROM user, sighting
WHERE user.email = sighting.user_email
GROUP BY email;

-- -- c
CREATE VIEW IF NOT EXISTS crow_sightings AS
SELECT common_name AS "Common Name(s)", CONCAT(common_name.genus_name, " ", common_name.species_epithet) AS "Scientific Name", COUNT(common_name) AS "Sightings"
FROM common_name
JOIN species ON common_name.genus_name = species.genus_name AND common_name.species_epithet = species.species_epithet
JOIN sighting ON common_name.genus_name = sighting.genus_name AND common_name.species_epithet = sighting.species_epithet
WHERE species.family_name = "Crows"
GROUP BY common_name;

-- 3
-- -- a
DELIMITER //
CREATE TRIGGER IF NOT EXISTS validate_email
    BEFORE INSERT ON user
    FOR EACH ROW
    BEGIN
        IF NEW.email NOT LIKE "_%@_%._%" THEN
            SIGNAL SQLSTATE '42000'
            SET MESSAGE_TEXT = 'Invalid email address';
        END IF;
    END;
//
DELIMITER ;

-- -- b
DELIMITER //
CREATE TRIGGER IF NOT EXISTS validate_home_position
    BEFORE INSERT ON user
    FOR EACH ROW
    BEGIN
        IF (NEW.home_latitude < -90 OR NEW.home_latitude > 90 OR NEW.home_longitude < -90 OR NEW.home_longitude > 90) AND (NEW.home_latitude != NULL OR NEW.home_longitude != NULL) THEN
            SIGNAL SQLSTATE '42000'
            SET MESSAGE_TEXT = 'Invalid position';
        END IF;
    END;
//

CREATE TRIGGER IF NOT EXISTS validate_position
    BEFORE INSERT ON sighting
    FOR EACH ROW
    BEGIN
        IF (NEW.latitude < -90 OR NEW.latitude > 90 OR NEW.longitude < -90 OR NEW.longitude > 90) AND (NEW.latitude != NULL OR NEW.longitude != NULL) THEN
            SIGNAL SQLSTATE '42000'
            SET MESSAGE_TEXT = 'Invalid position';
        END IF;
    END;
//
DELIMITER ;

-- 4
-- -- a
DELIMITER //
CREATE FUNCTION IF NOT EXISTS func_earth_distance (lat_1 DOUBLE, lon_1 DOUBLE, lat_2 DOUBLE, lon_2 DOUBLE) RETURNS DOUBLE
BEGIN
    DECLARE A DOUBLE;
    DECLARE lat_1_r DOUBLE;
    DECLARE lon_1_r DOUBLE;
    DECLARE lat_2_r DOUBLE;
    DECLARE lon_2_r DOUBLE;

    SET lat_1_r = RADIANS(lat_1);
    SET lon_1_r = RADIANS(lon_1);
    SET lat_2_r = RADIANS(lat_2);
    SET lon_2_r = RADIANS(lon_2);

    SET A = SIN((lat_2_r - lat_1_r) / 2) * SIN((lat_2_r - lat_1_r) / 2) + COS(lat_1_r) * COS(lat_2_r) * SIN((lon_2_r - lon_1_r) / 2) * SIN((lon_2_r - lon_1_r) / 2);
    RETURN 2 * 6371 * ATAN2(SQRT(A), SQRT(1 - A));
END;
//
DELIMITER ;

-- -- b
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS proc_add_user(email TEXT(320), display_name VARCHAR(512), home_latitude DOUBLE(4, 2), home_longitude DOUBLE(4, 2), password CHAR(64))
BEGIN
    DECLARE BIRDS CHAR(5);
    DECLARE salted_password CHAR(64);

    SET BIRDS = "BIRDS";

    SET salted_password = SHA2(CONCAT(password, BIRDS), 256);

    INSERT INTO user VALUES (email, display_name, home_latitude, home_longitude, salted_password);
END;
//
DELIMITER ;

-- -- c
DELIMITER //
CREATE FUNCTION IF NOT EXISTS func_valid_credentials (email TEXT(320), password CHAR(64)) RETURNS BOOLEAN
BEGIN
    DECLARE BIRDS VARCHAR(5);
    DECLARE salted_password VARCHAR(64);

    SET BIRDS = "BIRDS";

    SET salted_password = SHA2(CONCAT(password, BIRDS), 256);

    IF EXISTS (SELECT * FROM user WHERE user.email = email AND user.salted_password_hash = salted_password) THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
//
DELIMITER ;
