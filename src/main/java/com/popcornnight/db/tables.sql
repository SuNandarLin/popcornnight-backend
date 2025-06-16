-- Table users
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `is_anonymous` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

    --    Table: theatres
CREATE TABLE `theatres` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

--  Table: halls
CREATE TABLE `halls` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `hall_number` varchar(255) DEFAULT NULL,
  `seatno_grid` json DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_seats` int DEFAULT NULL,
  `theatre_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8fh78mrnfkvy5w77acyi9n0pd` (`theatre_id`),
  CONSTRAINT `FK8fh78mrnfkvy5w77acyi9n0pd` FOREIGN KEY (`theatre_id`) REFERENCES `theatres` (`id`)
)

-- Table: movies
CREATE TABLE `movies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `release_date` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

--  Table: show_times
CREATE TABLE `show_times` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_published` bit(1) DEFAULT NULL,
  `seatstatus_grid` json DEFAULT NULL,
  `timeslot` varchar(255) DEFAULT NULL,
  `timestamp` bigint DEFAULT NULL,
  `hall_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlwdyg27qinr4lfd9h2nlbmd8y` (`hall_id`),
  KEY `FK8j8e0814cfoypjrui0bl745ap` (`movie_id`),
  CONSTRAINT `FK8j8e0814cfoypjrui0bl745ap` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `FKlwdyg27qinr4lfd9h2nlbmd8y` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`)
)

--  Table: tickets
CREATE TABLE `tickets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` float DEFAULT NULL,
  `qrcode_url` varchar(255) DEFAULT NULL,
  `seat_number` json DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `showtime_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `qr_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfijobqym37inadqa0363d6xov` (`showtime_id`),
  KEY `FK4eqsebpimnjen0q46ja6fl2hl` (`user_id`),
  CONSTRAINT `FK4eqsebpimnjen0q46ja6fl2hl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKfijobqym37inadqa0363d6xov` FOREIGN KEY (`showtime_id`) REFERENCES `show_times` (`id`)
)

FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)