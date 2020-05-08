-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Update the ClassSection Table.
-- =============================================
/*
EXEC update_ClassSection 3, 'CSSE333', 02, 'Databases', 0, 2020, 'DemoUser'
select * from ClassSection
*/


USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'update_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [update_ClassSection]
GO

CREATE PROCEDURE update_ClassSection (
@ClassSectionID_1 int,
@CourseNumber_1 varchar(8), 
@SectionNumber_1 tinyint,
@ClassName_1 nvarchar(20), 
@IsPrivate_1 bit, 
@SchoolYear_1 smallint, 
@ParentUserID_1 varchar(20),
@Msg nvarchar(MAX) = null OUTPUT
)
AS
BEGIN TRY
	UPDATE ClassSection
	SET
	CourseNumber = @CourseNumber_1,
	SectionNumber = @SectionNumber_1,
	ClassName = @ClassName_1,
	IsPrivate = @IsPrivate_1,
	SchoolYear = @SchoolYear_1,
	ParentUserID = @ParentUserID_1

	WHERE ClassSectionID = @ClassSectionID_1
		SET @Msg = 'ClassSection Updated Successfully!'

END TRY
BEGIN CATCH  
    SELECT ERROR_MESSAGE() AS ErrorMessage;  
END CATCH;  
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_ClassSection] TO appUserCalendarDB;