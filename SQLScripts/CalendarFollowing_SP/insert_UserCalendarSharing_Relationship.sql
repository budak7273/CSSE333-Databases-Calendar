/*--------------------------------------------------------------

Adds a username-calendar relationship to the UserCalendarSharing table to indicate following that calendar

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [get_All_Followed_Calendars_Of_User] @Username_1 ='DemoUser' @CalendarID_2 = 7
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_UserCalendarSharing_Relationship') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_UserCalendarSharing_Relationship]
GO

-- Create procedure
CREATE PROCEDURE [insert_UserCalendarSharing_Relationship]
(@Username_1	[varchar](20),
 @CalendarID_2	[int])
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		RAISERROR('Username can not be null', 14, 1)
		RETURN 1
	END

	IF @CalendarID_2 IS NULL
	BEGIN
		RAISERROR('CalendarID can not be null', 14, 1)
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		RAISERROR('That Username does not exist in the database', 14, 1)
		RETURN 1
	END

	-- Check to see if Calendar ID is valid
	IF (SELECT Count(ClassCalendarID) from [ClassCalendar] WHERE ClassCalendarID = @CalendarID_2) != 1
	BEGIN
		RAISERROR('That ClassCalendarID does not exist in the database', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	INSERT INTO [UserCalendarSharing]
		       ([Username],		[ClassCalendarID])
		VALUES (@Username_1,	@CalendarID_2)

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the followed calendars for the user.', 14, @Status)
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
GRANT EXECUTE ON [insert_UserCalendarSharing_Relationship] TO appUserCalendarDB;