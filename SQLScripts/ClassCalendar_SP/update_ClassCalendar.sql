-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Update the Class Calendar Table.
-- =============================================
--Demo

/*
EXEC update_ClassCalendar
	@ClassCalendarID_1 = 7,
	@CalendarColor_1 = 4567,
	@ClassTime_2 = '10:00',
	@ClassName_3 = 'CSSE340',
	@ParentUserID_4 = 'DemoUser'
	
select * FROM ClassCalendar
*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'update_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [update_ClassCalendar]
GO

CREATE PROCEDURE update_ClassCalendar(
@ClassCalendarID_1 int,
@CalendarColor_1 int,
@ClassTime_2 time,
@ClassName_3 nvarchar(20),
@ParentUserID_4 varchar(20)
)
AS
IF NOT EXISTS 
(
SELECT ClassCalendarID
FROM ClassCalendar
WHERE 
ClassCalendarID = @ClassCalendarID_1
)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred updating the class calendar information.';  
	RETURN 99;  
END
ELSE
	UPDATE ClassCalendar
	SET

	CalendarColor = @CalendarColor_1,
	ClassTime = @ClassTime_2,
	ClassName = @ClassName_3,
	ParentUserID = @ParentUserID_4
	
	WHERE ClassCalendarID = @ClassCalendarID_1
	BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class calendar has been updated.';  
        RETURN 0;  
    END; 
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_ClassCalendar] TO appUserCalendarDB;

