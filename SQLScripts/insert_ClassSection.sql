USE CalendarDB
GO

CREATE PROCEDURE dbo.[insert_ClassSection]
@ClassSectionID_1 int, @CourseNumber_1 varchar(8), @SectionNumber_1 tinyint,
@ClassName_1 nvarchar(20), @IsPrivate_1 bit, @SchoolYear_1 smallint, 
@ParentUserID_1 varchar
AS

IF(@SectionNumber_1 is NULL)
BEGIN
	RAISERROR('Must specifiy SectionNumber!', 14, 1)
	RETURN
END

IF(@IsPrivate_1 is NULL)
BEGIN
	RAISERROR('Must specifiy if Private!', 14, 1)
	RETURN
END

IF(@SchoolYear_1 is NULL)
BEGIN
	RAISERROR('Must specifiy School Year!', 14, 1)
	RETURN
END

INSERT INTO ClassSection
(ClassSectionID, CourseNumber, SectionNumber, IsPrivate, SchoolYear, ParentUserID)
VALUES(@ClassSectionID_1, @CourseNumber_1, @ClassName_1, @IsPrivate_1, @SchoolYear_1,
@ParentUserID_1)

RETURN 0
