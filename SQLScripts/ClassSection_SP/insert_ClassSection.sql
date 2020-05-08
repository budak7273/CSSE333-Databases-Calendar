-- =============================================
-- Author:		Thomas Nandola
-- Create date: 5-1-2020
-- Description:	Inserting ClassSection Values into ClassSection Table
-- =============================================
-- Demo: Example with minimum parameters specified
/*
	EXEC [insert_ClassSection] 
	@CourseNumber_1 = 'CSSE220',
	@SectionNumber_2 = 04,
	@ClassName_3 = 'Databases',
	@IsPrivate_4 = 0,
	@SchoolYear_5 = 2020,
	@ParentUserID_6 = 'DemoUser'
	select * from ClassSection
*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_ClassSection]
GO

-- Create procedure
CREATE PROCEDURE insert_ClassSection(
@CourseNumber_1 varchar(8), 
@SectionNumber_2 tinyint,
@ClassName_3 nvarchar(20), 
@IsPrivate_4 bit,
@SchoolYear_5 smallint, 
@ParentUserID_6 varchar(20)
)
AS

	-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'

	--Check parameters that aren't allowed to be null
	IF @CourseNumber_1 is NULL
	BEGIN
		RAISERROR('Course Number cannot be null', 14, 1)
		RETURN 1
	END

	IF @SectionNumber_2 is NULL
	BEGIN
		RAISERROR('Section Number cannot be null', 14, 1)
		RETURN 1
	END

	IF @ClassName_3 is NULL
	BEGIN
		RAISERROR('Class Name cannot be null', 14, 1)
		RETURN 1
	END

	IF @IsPrivate_4 is NULL
	BEGIN
		RAISERROR('IsPrivate cannot be null', 14, 1)
		RETURN 1
	END

	IF @SchoolYear_5 is NULL
	BEGIN
		RAISERROR('School Year cannot be null', 14, 1)
		RETURN 1
	END

	IF @ParentUserID_6 is NULL
	BEGIN
		RAISERROR('ParentUserID cannot be null', 14, 1)
		RETURN 1
	END

	--Check max length on CourseNumber
	IF LEN(@CourseNumber_1) > 8
	BEGIN
		RAISERROR('Course Number above maximum of 20 characters', 14, 1)
		RETURN 1
	END

	--Check max length on ClassName
	IF LEN(@ClassName_3) > 20
	BEGIN
		RAISERROR('Class Name above maximum of 20 characters', 14, 1)
		RETURN 1
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_6) > 20
	BEGIN
		RAISERROR('ParentUserID above maximum of 20 characters', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	BEGIN
		INSERT INTO ClassSection
		(CourseNumber, SectionNumber, ClassName, IsPrivate, SchoolYear, ParentUserID)
		Values(@CourseNumber_1, @SectionNumber_2, @ClassName_3, @IsPrivate_4, @SchoolYear_5,
		@ParentUserID_6)
	END

	--End Actual Procedure

	-- Check for errors
		DECLARE @Status SMALLINT
		SET @Status = @@ERROR
		IF @Status <> 0 
		BEGIN
			-- Return error code to the calling program to indicate failure.
			PRINT 'An error occurred inserting the Class Section.'
			RETURN @Status
		END
		ELSE
		BEGIN
			-- Return 0 to the calling program to indicate success.
			PRINT 'The Class Section was inserted successfully.'
			RETURN 0
		END

GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [insert_ClassSection] TO appUserCalendarDB;