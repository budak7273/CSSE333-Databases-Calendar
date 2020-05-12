-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Delete from the ClassCalendar Table.
-- =============================================
--Demo

EXEC delete_ClassCalendar 26
select * from ClassCalendar

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'delete_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [delete_ClassCalendar]
GO

CREATE PROCEDURE delete_ClassCalendar(
@ClassCalendarID_1 int
)
AS
IF NOT EXISTS 
(
SELECT ClassCalendarID
FROM ClassCalendar
WHERE ClassCalendarID = @ClassCalendarID_1)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred deleting the class calendar information.';  
	RETURN 99;  
END
ELSE 
	DELETE FROM ClassCalendar
	WHERE ClassCalendarID = @ClassCalendarID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class calendar has been deleted.';  
        RETURN 0;  
    END;  

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [delete_ClassCalendar] TO appUserCalendarDB;

