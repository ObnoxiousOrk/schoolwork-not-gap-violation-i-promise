DROP TABLE IF EXISTS `sighting`;
DROP TABLE IF EXISTS `common_name`;
DROP TABLE IF EXISTS `species`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `family`;

CREATE TABLE `family` (
  `name` varchar(512) NOT NULL,
  PRIMARY KEY (`name`)
);

CREATE TABLE `user` (
  `email` varchar(320) NOT NULL,
  `display_name` varchar(512) NOT NULL,
  `home_latitude` double(4,2) DEFAULT NULL,
  `home_longitude` double(4,2) DEFAULT NULL,
  `salted_password_hash` char(64) NOT NULL,
  PRIMARY KEY (`email`)
);

CREATE TABLE `species` (
  `family_name` varchar(512) NOT NULL,
  `genus_name` varchar(512) NOT NULL,
  `species_epithet` varchar(512) NOT NULL,
  PRIMARY KEY (`genus_name`,`species_epithet`),
  FOREIGN KEY (`family_name`)
    REFERENCES `family` (`name`) 
);

CREATE TABLE `sighting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_email` varchar(320) NOT NULL,
  `date_spotted` date NOT NULL,
  `latitude` double(4,2) DEFAULT NULL,
  `longitude` double(4,2) DEFAULT NULL,
  `notes` varchar(1024) DEFAULT NULL,
  `genus_name` varchar(512) NOT NULL,
  `species_epithet` varchar(512) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`genus_name`, `species_epithet`)
    REFERENCES `species` (`genus_name`, `species_epithet`),
  FOREIGN KEY (`user_email`)
    REFERENCES `user` (`email`)
);

CREATE TABLE `common_name` (
  `common_name` varchar(512) NOT NULL,
  `genus_name` varchar(512) NOT NULL,
  `species_epithet` varchar(512) NOT NULL,
  PRIMARY KEY (`common_name`),
  FOREIGN KEY (`genus_name`, `species_epithet`)
    REFERENCES `species` (`genus_name`, `species_epithet`)
);

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

CREATE TRIGGER IF NOT EXISTS validate_home_position
    BEFORE INSERT ON user
    FOR EACH ROW
    BEGIN
        IF (NEW.home_latitude < -90 OR NEW.home_latitude > 90 OR NEW.home_longitude < -90 OR NEW.home_longitude > 90) OR (NEW.home_latitude != NULL OR NEW.home_longitude != NULL) THEN
                SIGNAL SQLSTATE '42000'
                SET MESSAGE_TEXT = 'Invalid position';
        END IF;
    END;
//

CREATE TRIGGER IF NOT EXISTS validate_position
    BEFORE INSERT ON sighting
    FOR EACH ROW
    BEGIN
        IF (NEW.latitude < -90 OR NEW.latitude > 90 OR NEW.longitude < -90 OR NEW.longitude > 90) OR (NEW.latitude != NULL OR NEW.longitude != NULL) THEN
                SIGNAL SQLSTATE '42000'
                SET MESSAGE_TEXT = 'Invalid position';
        END IF;
    END;
//

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

CREATE PROCEDURE IF NOT EXISTS proc_add_user(email VARCHAR(320), display_name VARCHAR(512), home_latitude DOUBLE(4, 2), home_longitude DOUBLE(4, 2), password VARCHAR(64))
BEGIN
    DECLARE BIRDS VARCHAR(5);
    DECLARE salted_password VARCHAR(64);

    SET BIRDS = "BIRDS";

    SET salted_password = SHA2(CONCAT(password, BIRDS), 256);

    INSERT INTO user VALUES (email, display_name, home_latitude, home_longitude, salted_password);
END;
//

CREATE FUNCTION IF NOT EXISTS func_valid_credentials (email VARCHAR(320), password VARCHAR(64)) RETURNS BOOLEAN
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

CALL proc_add_user('a@a.a', 'a', 0, 0, '961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506');