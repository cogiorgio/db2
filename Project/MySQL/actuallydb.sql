
CREATE DATABASE  IF NOT EXISTS `db2_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db2_project`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`id` int NOT NULL
AUTO_INCREMENT,
`username` varchar(45) NOT NULL,
`password` varchar(45) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `questionnaire` (
`id` int NOT NULL AUTO_INCREMENT,
`product` varchar(45) NOT NULL,
`date` date NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `review` (
`id` int NOT NULL AUTO_INCREMENT,
`user` int NOT NULL,
`questionnaire` int NOT NULL,
`product` int NOT NULL,
`sex` varchar(1) ,
`age` int ,
`level` int,
`points` int,
PRIMARY KEY (`id`),
KEY `user_idR` (`user`),
KEY `questionnaire_idR` (`questionnaire`),
CONSTRAINT `questionnaire_idR` FOREIGN KEY (`questionnaire`) REFERENCES `questionnaire` (`id`),
CONSTRAINT `user_idR` FOREIGN KEY (`user`) REFERENCES `user` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `question` (
`id` int NOT NULL AUTO_INCREMENT,
`text` varchar(45) NOT NULL,
`questionnaire` int NOT NULL,
PRIMARY KEY (`id`),
KEY `questionnaire_idQ` (`questionnaire`),
CONSTRAINT `questionnaire_idQ` FOREIGN KEY (`questionnaire`) REFERENCES `questionnaire` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `answer` (
`id` int NOT NULL AUTO_INCREMENT,
`lines` varchar(45) NOT NULL,
`question` int NOT null,
`review` int NOT NULL,
`date` date not null ,
KEY `question_idA` (`question`),
KEY `review_idA` (`review`),
PRIMARY KEY (`id`),
CONSTRAINT `question_idA` FOREIGN KEY (`question`) REFERENCES `question` (`id`) ,
CONSTRAINT `review_idA` FOREIGN KEY (`review`) REFERENCES `review` (`id`) 
)ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `blacklist` (
`id` int NOT NULL
AUTO_INCREMENT,
`badwords` varchar(45) NOT NULL,
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

