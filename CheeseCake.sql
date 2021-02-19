CREATE TRIGGER UpdatePointsOnAnswer 
AFTER INSERT ON answer
FOR EACH ROW
	UPDATE review 
    SET points = points + 1 
    WHERE id = NEW.review
    
delimiter //
CREATE TRIGGER UpdatePointsOnStatistical
BEFORE INSERT ON review
FOR EACH ROW
BEGIN
	DECLARE X INT DEFAULT 0;
	IF NEW.sex IS NOT NULL THEN SET X = X+2; 
    END IF;
	IF NEW.age IS NOT NULL THEN SET X = X+2; 
    END IF;
	IF NEW.level IS NOT NULL THEN SET X = X+2; 
    END IF;
	SET NEW.points = NEW.points + X;
END//
delimiter ;

delimiter //
CREATE TRIGGER BigBaddiesOut
BEFORE INSERT ON answer
FOR EACH ROW 
BEGIN
IF NEW.text LIKE "%can%" THEN 
	SET NEW.text = "hola";
	signal sqlstate '45000' set message_text = "You're using a BADWORD!";
END IF;
END//
delimiter ;

DROP TRIGGER IF EXISTS BigBaddiesOut