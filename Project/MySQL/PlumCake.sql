USE `projectdb`;

DELIMITER $$

USE `projectdb`$$
DROP TRIGGER IF EXISTS `projectdb`.`UpdatePointsOnAnswer` $$
DELIMITER ;

USE `projectdb`;

DELIMITER $$

USE `projectdb`$$
DROP TRIGGER IF EXISTS `projectdb`.`UpdatePointsOnAnswerDel` $$
DELIMITER ;

USE `projectdb`;

DELIMITER $$

USE `projectdb`$$
DROP TRIGGER IF EXISTS `projectdb`.`UpdatePointsOnStatistical` $$
DELIMITER ;
USE `projectdb`;

DELIMITER $$

USE `projectdb`$$
DROP TRIGGER IF EXISTS `projectdb`.`UpdatePointsOnStatisticalDel` $$
DELIMITER ; 

delimiter //
CREATE TRIGGER UpdatePointsOnAnswer 
AFTER INSERT ON answer
FOR EACH ROW
BEGIN
	DECLARE X INT DEFAULT 0;
	UPDATE user 
    SET points = points + 1 
    WHERE id = (SELECT R.user FROM review AS R WHERE
    R.id = NEW.review);
END //
delimiter ;
   
   delimiter //
CREATE TRIGGER UpdatePointsOnAnswerDel 
AFTER DELETE ON answer
FOR EACH ROW
BEGIN
	DECLARE X INT DEFAULT 0;
	UPDATE user 
    SET points = points - 1
    WHERE id = (SELECT R.user FROM review AS R WHERE
    R.id = OLD.review);
END//
delimiter;

 
delimiter //
CREATE TRIGGER UpdatePointsOnStatistical
BEFORE INSERT ON review
FOR EACH ROW
BEGIN
	DECLARE X INT DEFAULT 0;
	IF NEW.sex <> '\0' THEN SET X = X+2; 
    END IF;
	IF NEW.age <> 0 THEN SET X = X+2; 
    END IF;
	IF NEW.level <> '\0' THEN SET X = X+2; 
    END IF;
    UPDATE user
	SET points = points + X
    WHERE id = NEW.user;
END//
delimiter ;

delimiter //
CREATE TRIGGER UpdatePointsOnStatisticalDel
AFTER DELETE ON review
FOR EACH ROW
BEGIN
	DECLARE X INT DEFAULT 0;
	IF OLD.sex <> '\0' THEN SET X = X-2; 
    END IF;
	IF OLD.age <> 0 THEN SET X = X-2; 
    END IF;
	IF OLD.level <> '\0' THEN SET X = X-2; 
    END IF;
    UPDATE user
	SET points = points + X
    WHERE id = OLD.user;
END//
delimiter ;

