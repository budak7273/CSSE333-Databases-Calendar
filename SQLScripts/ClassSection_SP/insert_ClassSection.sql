-- =============================================
-- Author:		Thomas Nandola
-- Create date: 5-1-2020
-- Description:	Inserting ClassSection Values into ClassSection Table
-- =============================================
-- Demo: Example with minimum parameters specified
	/*
	EXEC [insert_ClassSection] 
	@CourseNumber_1 = 'CSSE320',
	@SectionNumber_2 = 05,
	@ClassName_3 = 'AdvancedDatabases',
	@IsPrivate_4 = 0,
	@SchoolYear_5 = 2020,
	@ParentUserID_6 = 'DemoUsers'
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
	--Check for ParentUserID
	IF NOT EXISTS 
	(
	SELECT ParentUserID
	FROM ClassSection
	WHERE 
	ParentUserID = @ParentUserID_6
	)
	BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred, ParentUserID does not exist.';  
	RETURN 99;  
	END

	--Check parameters that aren't allowed to be null
	IF @CourseNumber_1 is NULL
	BEGIN
		PRINT('Course Number cannot be null')
		RETURN 1
	END

	IF @SectionNumber_2 is NULL
	BEGIN
		PRINT('Section Number cannot be null')
		RETURN 2
	END

	IF @ClassName_3 is NULL
	BEGIN
		PRINT('Class Name cannot be null')
		RETURN 3
	END

	IF @IsPrivate_4 is NULL
	BEGIN
		PRINT('IsPrivate cannot be null')
		RETURN 4
	END

	IF @SchoolYear_5 is NULL
	BEGIN
		PRINT('School Year cannot be null')
		RETURN 5
	END

	IF @ParentUserID_6 is NULL
	BEGIN
		PRINT('ParentUserID cannot be null')
		RETURN 6
	END

	--Check max length on CourseNumber
	IF LEN(@CourseNumber_1) > 8
	BEGIN
		PRINT('Course Number above maximum of 20 characters')
		RETURN 8
	END

	--Check max length on ClassName
	IF LEN(@ClassName_3) > 20
	BEGIN
		PRINT('Class Name above maximum of 20 characters')
		RETURN 9
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_6) > 20
	BEGIN
		PRINT('ParentUserID above maximum of 20 characters')
		RETURN 10
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