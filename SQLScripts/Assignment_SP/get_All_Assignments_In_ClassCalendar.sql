/*--------------------------------------------------------------

Gets all assignments that belong to a specific ClassCalendar

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [get_All_Assignments_In_ClassCalendar] @ParentClassCalendarID_1 = 2
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'get_All_Assignments_In_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [get_All_Assignments_In_ClassCalendar]
GO

-- Create procedure
CREATE PROCEDURE [get_All_Assignments_In_ClassCalendar]
(@ParentClassCalendarID_1	[int])
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @ParentClassCalendarID_1 IS NULL
	BEGIN
		RAISERROR('ParentClassCalendarID can not be null', 14, 1)
		RETURN 1
	END

	-- Check to see if ParentClassCalendarID is valid
	IF (SELECT Count(ClassCalendarID) from ClassCalendar WHERE ClassCalendarID = @ParentClassCalendarID_1) != 1
	BEGIN
		RAISERROR('The ClassCalendarID does not exist in the database', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	SELECT *
	FROM [Assignment] asn
	WHERE asn.ParentClassCalendarID = @ParentClassCalendarID_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the assignments.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignments were fetched successfully.'
		RETURN 0
	END
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [get_All_Assignments_In_ClassCalendar] TO appUserCalendarDB;