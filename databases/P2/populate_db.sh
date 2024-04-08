# ssh dm319@dm319.teaching.cs.st-andrews.ac.uk
# -- G4.1M2Tiu54RQ3
mysql -u dm319 --password="G4.1M2Tiu54RQ3" -e "SOURCE /cs/home/dm319/Documents/CS3101/P2/schema.sql;" dm319_CS3101_P2

mysql -u dm319 --password="G4.1M2Tiu54RQ3" -e "LOAD DATA LOCAL INFILE '/cs/home/dm319/Documents/CS3101/P2/families.csv' INTO TABLE family FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"';" dm319_CS3101_P2

mysql -u dm319 --password="G4.1M2Tiu54RQ3" -e "LOAD DATA LOCAL INFILE '/cs/home/dm319/Documents/CS3101/P2/species.csv' INTO TABLE species FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"';" dm319_CS3101_P2

mysql -u dm319 --password="G4.1M2Tiu54RQ3" -e "LOAD DATA LOCAL INFILE '/cs/home/dm319/Documents/CS3101/P2/common_names.csv' INTO TABLE common_name FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"';" dm319_CS3101_P2

mysql -u dm319 --password="G4.1M2Tiu54RQ3" -e "LOAD DATA LOCAL INFILE '/cs/home/dm319/Documents/CS3101/P2/users.csv' INTO TABLE user FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"';" dm319_CS3101_P2

mysql -u dm319 --password="G4.1M2Tiu54RQ3" -e "LOAD DATA LOCAL INFILE '/cs/home/dm319/Documents/CS3101/P2/sightings.csv' INTO TABLE sighting FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"';" dm319_CS3101_P2
