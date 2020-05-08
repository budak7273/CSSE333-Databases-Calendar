-- =============================================
-- Author:		Thomas Nandola
-- Create date: 5-1-2020
-- Description:	Inserting ClassSection Values into ClassSection Table
-- =============================================
/*
EXEC insert_ClassSection 'CSSE333', 01, 'Databases', 0, 2020, 'DemoUser'

select * from ClassSection
*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insert_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [insert_ClassSection]
GO

CREATE PROCEDURE insert_ClassSection(
@CourseNumber_1 varchar(8), 
@SectionNumber_1 tinyint,
@ClassName_1 nvarchar(20), 
@IsPrivate_1 bit, 
@SchoolYear_1 smallint, 
@ParentUserID_1 varchar(20)
)
AS
BEGIN
	INSERT INTO ClassSection
	(CourseNumber, SectionNumber, ClassName, IsPrivate, SchoolYear, ParentUserID)
	Values(@CourseNumber_1, @SectionNumber_1, @ClassName_1, @IsPrivate_1, @SchoolYear_1,
	@ParentUserID_1)
END

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [insert_ClassSection] TO appUserCalendarDB;