USE CalendarDB
GO
DROP PROCEDURE insert_ClassSection
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Thomas Nandola
-- Create date: 5-1-2020
-- Description:	Inserting ClassSection Values into ClassSection Table
-- =============================================
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

EXEC insert_ClassSection 'CSSE333', 01, 'Databases', 0, 2020, 'DemoUser'

select * from ClassSection


 