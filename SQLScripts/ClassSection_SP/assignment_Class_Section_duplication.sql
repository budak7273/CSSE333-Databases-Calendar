USE CalendarDB
GO

ALTER TRIGGER assignment__Class_Section_duplication ON Assignment
AFTER INSERT
AS 
	DECLARE @inserted_ClassCalendarID int, @ClassSectionID int;

	SET @inserted_ClassCalendarID = (SELECT TOP 1 ParentClassCalendarID FROM inserted)

	
	IF (SELECT Count(*) FROM ClassSection WHERE ClassCalendarID = @inserted_ClassCalendarID) = 1 
	BEGIN
		SET @ClassSectionID = (SELECT TOP 1 ClassSectionID FROM ClassSection WHERE ClassCalendarID = @inserted_ClassCalendarID)
		INSERT INTO Assignment(EventName, EventDate, [Type], ParentClassSectionID, EventDescription, ParentClassCalendarID, EventProgress, EventSpecificColor) 
			SELECT i.EventName, i.EventDate, i.[Type], i.ParentClassSectionID, i.EventDescription, cc.ClassCalendarID as ParentClassCalendarID, 0, cc.CalendarColor
				FROM inserted i 
					INNER JOIN ClassCalendar cc
						ON cc.ClassSectionID = @ClassSectionID
		RAISERROR('Trigger Fired', 14, 1);
	END	