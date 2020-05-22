/*--------------------------------------------------------------

Gets a list of all ClassCalendars, their Name, and their authoring User

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [get_user_ClassCalendar_List] @Username_1 ='DemoUser'
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'get_user_ClassCalendar_List') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [get_user_ClassCalendar_List]
GO

-- Create procedure
CREATE PROCEDURE [get_user_ClassCalendar_List]
(@Username_1	[varchar](20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 2
	END

	--Actual Procedure--

	SELECT DISTINCT ccal.ClassCalendarID as [CalendarID], ccal.ClassName as [Name], ccal.ParentUserID as [Creator]
	FROM [ClassCalendar] ccal
		LEFT JOIN [UserCalendarSharing] ucalShare
		ON ccal.ClassCalendarID = ucalShare.ClassCalendarID
	WHERE ccal.ParentUserID = @Username_1 OR ucalShare.Username = @Username_1
	ORDER BY ccal.ClassCalendarID

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred getting the class calendar list.'
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
GRANT EXECUTE ON [get_user_ClassCalendar_List] TO appUserCalendarDB;