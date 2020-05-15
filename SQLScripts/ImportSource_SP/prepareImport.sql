/*--------------------------------------------------------------

Creates a new ImportSource and ClassCalendar for use in the iCal import process, returning the ParentCalendarID of the created calendar.

Takes the Username of the user performing the import and a name to use for the ClassCalendar

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [prepareImport] @Username_1 ='DemoUser', @ClassName_2 = 'DemoClassImport'
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'prepareImport') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [prepareImport]
GO

-- Create procedure
CREATE PROCEDURE [prepareImport]
(@Username_1	[varchar](20),
 @ClassName_2	[varchar](20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		RAISERROR('Username can not be null', 14, 1)
		RETURN 1
	END

	IF @ClassName_2 IS NULL OR LEN(@ClassName_2) = 0
	BEGIN
		RAISERROR('Name can not be null or empty', 14, 1)
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		RAISERROR('That Username does not exist in the database', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	--DECLARE @NewCalendarIDTable table([NewId] INT)
	DECLARE @NewCalendarID INT
	DECLARE @NewImportSourceID INT

	BEGIN TRANSACTION
	--Create the new ClassCalendar to be imported into
	INSERT INTO [ClassCalendar]
	--OUTPUT inserted.ClassCalendarID INTO @NewCalendarIDTable
		       ([CalendarColor],	[ClassTime],	[ClassName],	[ParentUserID])
		VALUES (0,					{t '12:00:00'},	@ClassName_2,	@Username_1)
	
	SET @NewCalendarID = SCOPE_IDENTITY()
	PRINT 'New calendar ID is'
	PRINT @NewCalendarID

	--Create the new ImportSource
	INSERT INTO [ImportSource]
		       ([Type],	[ParentCalendarID])
		VALUES (1,		@NewCalendarID)

	SET @NewImportSourceID = SCOPE_IDENTITY()
	PRINT 'New ImportSource ID is'
	PRINT @NewImportSourceID

	--return values of the SPROC
	SELECT @NewCalendarID AS NewCalendarID, @NewImportSourceID AS NewImportSourceID
	COMMIT TRANSACTION

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
GRANT EXECUTE ON [prepareImport] TO appUserCalendarDB;