-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Insert into the ClassCalendar Table.
-- =============================================
-- Demo: Example with minimum parameters specified

	EXEC [insert_ClassCalendar] 
	@CalendarColor_1 = 1234,
	@ClassTime_2 = '11:00',
	@ClassName_3 = 'CSSE230',
	@ParentUserID_4 = 'DemoUser'

	select * from ClassCalendar



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

	--Check paramters that aren't allowed to be null
	IF @CalendarColor_1 IS NULL
	BEGIN
		SET @CalendarColor_1 = 333333
	END

	IF @ClassTime_2 is NULL
	BEGIN
		RAISERROR('ClassTime cannot be null', 14, 1)
		RETURN 1
	END

	IF @ClassName_3 is NULL
	BEGIN
		RAISERROR('ClassName cannot be null', 14, 1)
		RETURN 1
	END

	IF @ParentUserID_4 is NULL
	BEGIN
		RAISERROR('ParentUserID cannot be null', 14, 1)
	END

	--Check if CalendarColor is positive int
	IF @CalendarColor_1 < 0
	BEGIN
		RAISERROR('CalendarColor must be positive integer of 0 or greater', 14, 1)
		RETURN 1
	END

	--Check max length on ClassName
	IF LEN(@ClassName_3) > 20
	BEGIN
		RAISERROR('ClassName above maximum of 20 characters', 14, 1)
		RETURN 1
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_4) > 20
	BEGIN
		RAISERROR('ParentUserID above maximum of 20 characters', 14, 1)
		RETURN 1
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
		RAISERROR('An error occurred inserting the Class Calendar.', 14, @Status)
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