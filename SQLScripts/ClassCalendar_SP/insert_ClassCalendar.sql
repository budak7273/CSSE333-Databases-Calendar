-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Insert into the ClassCalendar Table.
-- =============================================
-- Demo: Example with minimum parameters specified
/*
	EXEC [insert_ClassCalendar] 
	@CalendarColor_1 = 1234,
	@ClassTime_2 = '11:00',
	@ClassName_3 = 'CSSE230',
	@ParentUserID_4 = 'DemoUser'

	select * from ClassCalendar
*/


USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_ClassCalendar]
GO

-- Create procedure
CREATE PROCEDURE insert_ClassCalendar(
	@CalendarColor_1 int,
	@ClassTime_2 time,
	@ClassName_3 nvarchar(20),
	@ParentUserID_4 varchar(20)
)
AS

	-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'

	--Check for ParentUserID
	IF NOT EXISTS 
	(
	SELECT ParentUserID
	FROM ClassCalendar
	WHERE 
	ParentUserID = @ParentUserID_4
	)
	BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred, ParentUserID does not exist.';  
	RETURN 99;  
	END

	--Check paramters that aren't allowed to be null
	IF @CalendarColor_1 IS NULL
	BEGIN
		SET @CalendarColor_1 = 333333
	END

	IF @ClassTime_2 is NULL
	BEGIN
		PRINT('ClassTime cannot be null')
		RETURN 2
	END

	IF @ClassName_3 is NULL
	BEGIN
		PRINT('ClassName cannot be null')
		RETURN 3
	END

	IF @ParentUserID_4 is NULL
	BEGIN
		PRINT('ParentUserID cannot be null')
		RETURN 4
	END

	--Check if CalendarColor is positive int
	IF @CalendarColor_1 < 0
	BEGIN
		PRINT('CalendarColor must be positive integer of 0 or greater')
		RETURN 5
	END

	--Check max length on ClassName
	IF LEN(@ClassName_3) > 20
	BEGIN
		PRINT('ClassName above maximum of 20 characters')
		RETURN 6
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_4) > 20
	BEGIN
		PRINT('ParentUserID above maximum of 20 characters')
		RETURN 7
	END

	--Actual Procedure--

	BEGIN
		INSERT INTO ClassCalendar(
		CalendarColor,
		ClassTime,
		ClassName,
		ParentUserID)

		VALUES(
		@CalendarColor_1,
		@ClassTime_2,
		@ClassName_3,
		@ParentUserID_4)
	END

	--End Actual Procedure

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT('An error occurred inserting the Class Calendar.')
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The Class Calendar was inserted successfully.'
		RETURN 0
	END
	
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [insert_ClassCalendar] TO appUserCalendarDB;