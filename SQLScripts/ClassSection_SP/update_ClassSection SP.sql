-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Update the ClassSection Table.
-- =============================================

EXEC update_ClassSection 3, 'CSSE333', 02, 'Databases', 0, 2020, 'DemoUser'
select * from ClassSection

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'update_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [update_ClassSection]
GO

CREATE PROCEDURE update_ClassSection (
@ClassSectionID_1 int,
@CourseNumber_1 varchar(8), 
@SectionNumber_2 tinyint,
@ClassName_3 nvarchar(20), 
@IsPrivate_4 bit, 
@SchoolYear_5 smallint, 
@ParentUserID_6 varchar(20),
@Msg nvarchar(MAX) = null OUTPUT
)
AS
BEGIN TRY
	UPDATE ClassSection
	SET
	CourseNumber = @CourseNumber_1,
	SectionNumber = @SectionNumber_2,
	ClassName = @ClassName_3,
	IsPrivate = @IsPrivate_4,
	SchoolYear = @SchoolYear_5,
	ParentUserID = @ParentUserID_6

	WHERE ClassSectionID = @ClassSectionID_1
		SET @Msg = 'ClassSection Updated Successfully!'

END TRY
BEGIN CATCH  
    SELECT ERROR_MESSAGE() AS ErrorMessage;  
END CATCH;  
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_ClassSection] TO appUserCalendarDB;