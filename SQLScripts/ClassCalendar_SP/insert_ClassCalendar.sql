-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Insert into the ClassCalendar Table.
-- =============================================
-- Demo: Example with minimum parameters specified
	EXEC [insert_ClassCalendar] 
	@CalendarColor_1 = 55555,
	@ClassTime_1 = '9:00',
	@ClassName_1 = 'CSSE220',
	@ParentUserID_1 = 'DemoUser'

	select * from ClassCalendar



USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_ClassCalendar]
GO

-- Create procedure
CREATE PROCEDURE insert_ClassCalendar(
	@CalendarColor_1 [int],
	@ClassTime_1 [time],
	@ClassName_1 [nvarchar],
	@ParentUserID_1 [varchar]
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

	IF @ClassTime_1 is NULL
	BEGIN
		RAISERROR('ClassTime cannot be null', 14, 1)
		RETURN 1
	END

	IF @ClassName_1 is NULL
	BEGIN
		RAISERROR('ClassName cannot be null', 14, 1)
		RETURN 1
	END

	IF @ParentUserID_1 is NULL
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
	IF LEN(@ClassName_1) > 20
	BEGIN
		RAISERROR('ClassName above maximum of 20 characters', 14, 1)
		RETURN 1
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_1) > 20
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
		@ClassTime_1,
		@ClassName_1,
		@ParentUserID_1)
	END

	--End Actual Procedure

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT 'An error occurred inserting the Class Calendar.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The Class Calendar was inserted successfully.'
		RETURN 0
	END
	
GO
 




	
