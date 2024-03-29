/*--------------------------------------------------------------

Gets Calendars that a user Follows

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [get_All_Followed_Calendars_Of_User] @Username_1 ='DemoUser'
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'get_All_Followed_Calendars_Of_User') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [get_All_Followed_Calendars_Of_User]
GO

-- Create procedure
CREATE PROCEDURE [get_All_Followed_Calendars_Of_User]
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

	SELECT ccal.ClassCalendarID as [CalendarID], ccal.ClassName as [Name], ccal.ParentUserID as [Creator]
	FROM [UserCalendarSharing] calShare
		INNER JOIN [ClassCalendar] ccal
		ON ccal.ClassCalendarID = calShare.ClassCalendarID
	WHERE calShare.Username = @Username_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred getting the followed calendars for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The followed calendars were fetched successfully for the user.'
		RETURN 0
	END
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [get_All_Followed_Calendars_Of_User] TO appUserCalendarDB;