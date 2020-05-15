/*--------------------------------------------------------------

Gets a list of all ClassCalendars, their Name, and their authoring User

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [get_ClassCalendar_List]
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'get_ClassCalendar_List') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [get_ClassCalendar_List]
GO

-- Create procedure
CREATE PROCEDURE [get_ClassCalendar_List]
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--

	SELECT ccal.ClassCalendarID as [CalendarID], ccal.ClassName as [Name], ccal.ParentUserID as [Creator]
	FROM [ClassCalendar] ccal

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the class calendar list.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The class calendar list was fetched successfully.'
		RETURN 0
	END
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [get_ClassCalendar_List] TO appUserCalendarDB;