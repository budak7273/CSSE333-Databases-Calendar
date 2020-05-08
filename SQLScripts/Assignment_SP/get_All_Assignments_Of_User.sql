/*--------------------------------------------------------------

Gets all assignments that belong to a specific Useranme via the ClassCalendars they have created

-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [get_All_Assignments_Of_User] @Username_1 ='DemoUser'
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'get_All_Assignments_Of_User') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [get_All_Assignments_Of_User]
GO

-- Create procedure
CREATE PROCEDURE [get_All_Assignments_Of_User]
(@Username_1	varchar(20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		RAISERROR('Username can not be null', 14, 1)
		RETURN 1
	END

	PRINT @Username_1
	PRINT LEN(@Username_1)
	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		RAISERROR('That Username does not exist in the database', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	SELECT *
	FROM [Assignment] asn
		INNER JOIN [ClassCalendar] ccal
	ON asn.ParentClassCalendarID = ccal.ClassCalendarID
	WHERE ccal.ParentUserID = @Username_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT 'An error occurred getting the assignments for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignments were fetched successfully for the user.'
		RETURN 0
	END
GO