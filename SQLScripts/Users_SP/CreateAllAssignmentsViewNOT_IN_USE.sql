USE CalendarDB
GO

CREATE VIEW AllAssignmentsForUser AS
	SELECT AssignmentID, EventName, EventProgress, Type, EventSpecificColor, ParentClassCalendarID, ParentClassSectionID, ImportSourceID, CalendarColor, ClassTime, ClassName, ParentUserID AS UserID
		FROM Assignment A
			INNER JOIN ClassCalendar CC
				ON A.ParentClassCalendarID = CC.ClassCalendarID
