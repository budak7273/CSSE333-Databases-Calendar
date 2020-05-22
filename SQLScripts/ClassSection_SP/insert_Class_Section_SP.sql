USE [CalendarDB]
GO

/****** Object:  StoredProcedure [dbo].[insert_ClassSection]    Script Date: 5/22/2020 8:47:58 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
ALTER PROCEDURE [dbo].[insert_ClassSection](
@CourseNumber_1 varchar(8), 
@SectionNumber_2 tinyint,
@ClassName_3 nvarchar(20), 
@IsPrivate_4 bit,
@SchoolYear_5 smallint, 
@ParentUserID_6 varchar(20),
@ClassCalenddarID_7 int
)
AS

	-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'
	--Check for ParentUserID
	IF NOT EXISTS 
	(
	SELECT Username
	FROM [User]
	WHERE 
	Username = @ParentUserID_6
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

	IF @ClassCalenddarID_7 is NULL
	BEGIN
		PRINT('ClassCalendarID cannot be null')
		RETURN 7
	END

	--Actual Procedure--

	BEGIN

		INSERT INTO ClassSection
		(CourseNumber, SectionNumber, ClassName, IsPrivate, SchoolYear, ParentUserID, ClassCalendarID)
		Values(@CourseNumber_1, @SectionNumber_2, @ClassName_3, @IsPrivate_4, @SchoolYear_5, @ParentUserID_6, @ClassCalenddarID_7)
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


