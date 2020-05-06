/*--------------------------------------------------------------

Inserts a listing into OrderDetails with the specified info, and updates the stock quantity for that order in the Products table to reflect the product being sold

Params:
@OrderID_1 [int]
@ProductID_2 [int]
(optional) @UnitPrice_3 [money] >= 0
@Quantity_4 [smallint] >= 1
(optional) @Discount_5 [real] >= 0, <= 1
-------------------------------------------
Demo:
DECLARE @Status SMALLINT
EXEC @Status = [insert_Assignment]
SELECT Status = @Status
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_Assignment') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_Assignment]
GO

-- Create procedure
CREATE PROCEDURE [insert_Assignment]
(@EventName_1				[nvarchar] = null,
 @EventDate_2				[datetime],
 @EventProgress_3			[tinyint] = 0,
 @Type_4					[nvarchar] = null,
 @EventSpecificColor_5		[int] = null,
 @ParentClassCalendarID_6	[int],
 @ParentClassSectionID_7	[int] = null, --TODO were we going to be removing this, since there is already a link to it via the class calendar?
 @ImportSourceID_8			[int] = null)
AS
	-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'

	--Check parameters that aren't allowed to be null
	IF @EventDate_2 IS NULL
	BEGIN
		RAISERROR('EventDate can not be null', 14, 1)
		RETURN 1
	END

	IF @ParentClassCalendarID_6 IS NULL
	BEGIN
		RAISERROR('ParentClassCalendarID can not be null', 14, 1)
		RETURN 1
	END

	--Check max length on EventName
	IF LEN(@EventName_1) > 75
	BEGIN
		RAISERROR('EventName above maximum of 75 characters', 14, 1)
		RETURN 1
	END

	--Check max length on Type
	IF LEN(@Type_4) > 20
	BEGIN
		RAISERROR('Type above maximum of 20 characters', 14, 1)
		RETURN 1
	END

	--If @EventProgress_3 is given as null, set it to 0
	IF @EventProgress_3 IS NULL
	BEGIN
		SET @EventProgress_3 = 0
	END
	--Check bounds on progress
	ELSE IF @EventProgress_3 < 0 OR @EventProgress_3 > 100
	BEGIN
		RAISERROR('EventProgress outside of bounds: 0 to 100 inclusive', 14, 1)
		RETURN 1
	END

	--TODO verify valid color? or trust valid

	--Actual Procedure--

	INSERT INTO [Assignment]
		       ([EventName],	[EventDate],	[EventProgress],	[Type],		[EventSpecificColor],	[ParentClassCalendarID],	[ParentClassSectionID],		[ImportSourceID])
		VALUES (@EventName_1,	@EventDate_2,	@EventProgress_3,	@Type_4,	@EventSpecificColor_5,	@ParentClassCalendarID_6,	@ParentClassSectionID_7,	@ImportSourceID_8)

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT 'An error occurred inserting the assignment.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignment was inserted successfully.'
		RETURN 0
	END
GO