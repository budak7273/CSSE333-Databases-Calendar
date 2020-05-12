-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Update the ClassSection Table.
-- =============================================
Demo:
/*
EXEC update_ClassSection 4, 'CSSE333', 02, 'Databases', 0, 2020, 'DemoUser'
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
@SectionNumber_2 tinyint,
@ClassName_3 nvarchar(20), 
@IsPrivate_4 bit, 
@SchoolYear_5 smallint, 
@ParentUserID_6 varchar(20)
)
AS
IF NOT EXISTS 
(
SELECT ClassSectionID
FROM ClassSection
WHERE 
ClassSectionID = @ClassSectionID_1
)

BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred updating the class section information.';  
	RETURN 99;  
END
ELSE 
	UPDATE ClassSection
	SET
	CourseNumber = @CourseNumber_1,
	SectionNumber = @SectionNumber_2,
	ClassName = @ClassName_3,
	IsPrivate = @IsPrivate_4,
	SchoolYear = @SchoolYear_5,
	ParentUserID = @ParentUserID_6

	WHERE ClassSectionID = @ClassSectionID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class section has been updated.';  
        RETURN 0;  
    END;  
GO



-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_ClassSection] TO appUserCalendarDB;