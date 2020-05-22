/*--------------------------------------------------------------

Inserts an assignment with the given specifications.

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
DECLARE @tmp DATETIME
SET @tmp = GETDATE()

EXEC [insert_Assignment] 
	@EventDate_2=@tmp,
	@ParentClassCalendarID_6=2
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_Assignment') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_Assignment]
GO

-- Create procedure
CREATE PROCEDURE [insert_Assignment]
(@EventName_1				[nvarchar](75) = null,
 @EventDate_2				[datetime],
 @EventProgress_3			[tinyint] = 0,
 @Type_4					[nvarchar](20) = null,
 @EventSpecificColor_5		[int] = null,
 @ParentClassCalendarID_6	[int],
 @ParentClassSectionID_7	[int] = null,
 @ImportSourceID_8			[int] = null,
 @EventDescription_9		[varchar](257) = null)
AS
	-- Suppress row count messages
	SET NOCOUNT ON

	-- For debugging datetime recieved
	/*DECLARE @a nvarchar(20) = CAST(@EventDate_2 AS VARCHAR(20))
	RAISERROR('Date on import is %s', 18, 1, @a)
	*/

	--Check parameters that aren't allowed to be null
	IF @EventDate_2 IS NULL
	BEGIN
		PRINT N'EventDate can not be null'
		RETURN 1
	END

	IF @ParentClassCalendarID_6 IS NULL
	BEGIN
		PRINT N'ParentClassCalendarID can not be null'
		RETURN 1
	END

	--Check max length on EventName
	IF LEN(@EventName_1) > 75
	BEGIN
		PRINT N'EventName above maximum of 75 characters'
		RETURN 1
	END

	--Check max length on Type
	IF LEN(@Type_4) > 20
	BEGIN
		PRINT N'Type above maximum of 20 characters'
		RETURN 1
	END

	--Check max length on EventDescription
	IF LEN(@EventDescription_9) > 257
	BEGIN
		PRINT N'EventDescription above maximum of 257 characters'
		RETURN 1
	END

	--Set @ParentClassSectionID_7 to null class section if -1
	IF @ParentClassSectionID_7 = -1
	BEGIN
		SET @ParentClassSectionID_7 = null
	END

	--If @EventProgress_3 is given as null, set it to 0
	IF @EventProgress_3 IS NULL
	BEGIN
		SET @EventProgress_3 = 0
	END
	--Check bounds on progress
	ELSE IF @EventProgress_3 < 0 OR @EventProgress_3 > 100
	BEGIN
		PRINT N'EventProgress outside of bounds: 0 to 100 inclusive'
		RETURN 1
	END

	--Actual Procedure--

	INSERT INTO [Assignment]
		       ([EventName],	[EventDate],	[EventProgress],	[Type],		[EventSpecificColor],	[ParentClassCalendarID],	[ParentClassSectionID],		[ImportSourceID],	[EventDescription])
		VALUES (@EventName_1,	@EventDate_2,	@EventProgress_3,	@Type_4,	@EventSpecificColor_5,	@ParentClassCalendarID_6,	@ParentClassSectionID_7,	@ImportSourceID_8,	@EventDescription_9)

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred inserting the assignment.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignment was inserted successfully.'
		RETURN 0
	END
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [insert_Assignment] TO appUserCalendarDB;