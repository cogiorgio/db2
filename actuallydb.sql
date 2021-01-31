
CREATE DATABASE  IF NOT EXISTS `db_MyApp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_MyApp`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`id` int(11) NOT NULL
AUTO_INCREMENT,
`username` varchar(45) NOT NULL,
`password` varchar(45) NOT NULL,
`lastLog` date NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product` (
`id` int(11) NOT NULL
AUTO_INCREMENT,
name varchar(45) NOT NULL,
image_path varchar(45) NOT NULL,
`date` date not null ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `review` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user` int NOT NULL,
`product` int NOT NULL,
`sex` varchar(1) ,
`age` int(3) ,
`level` int(1),
PRIMARY KEY (`id`),
KEY `user_idR` (`user`),
KEY `product_idR` (`product`),
CONSTRAINT `product_idR` FOREIGN KEY (`product`) REFERENCES `product` (`id`),
CONSTRAINT `user_idR` FOREIGN KEY (`user`) REFERENCES `user` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `question` (
`id` int(11) NOT NULL
AUTO_INCREMENT,
product int NOT NULL,
line varchar(45) NOT NULL,
date date not null ,
PRIMARY KEY (`id`)

) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE `answer` (
`id` int(11) NOT NULL
AUTO_INCREMENT,
line varchar(45) NOT NULL,
question int(45) NOT null,
review int NOT NULL,
`date` date not null ,
KEY `question_idA` (`question`),
KEY `review_idA` (`review`),
PRIMARY KEY (`id`),
CONSTRAINT `question_idA` FOREIGN KEY (`question`) REFERENCES `question` (`id`) ,
CONSTRAINT `review_idA` FOREIGN KEY (`review`) REFERENCES `review` (`id`) 
)ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;








CREATE TABLE `blacklist` (
`id` int(11) NOT NULL
AUTO_INCREMENT,
badwords varchar(45) NOT NULL,
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

