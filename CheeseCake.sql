CREATE TRIGGER UpdatePointsOnAnswer 
AFTER INSERT ON answer
FOR EACH ROW
	UPDATE user 
    SET points = points + 1 
    WHERE id = (SELECT R.user FROM review AS R WHERE
    R.id = NEW.review)
    
    
CREATE TRIGGER UpdatePointsOnAnswerDel 
AFTER DELETE ON answer
FOR EACH ROW
	UPDATE user 
    SET points = points -1
    WHERE id = (SELECT R.user FROM review AS R WHERE
    R.id = OLD.review)
    
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
	IF OLD.sex IS NOT NULL THEN SET X = X-2; 
    END IF;
	IF OLD.age IS NOT NULL THEN SET X = X-2; 
    END IF;
	IF OLD.level IS NOT NULL THEN SET X = X-2; 
    END IF;
    UPDATE user
	SET points = points + X
    WHERE id = OLD.user;
END//
delimiter ;


/*
CREATE TRIGGER DeleteAsw
BEFORE DELETE ON review
FOR EACH ROW
DELETE FROM answer AS a
WHERE a.review = OLD.id
*/
