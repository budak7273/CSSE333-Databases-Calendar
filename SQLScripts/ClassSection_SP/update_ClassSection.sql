-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Update the ClassSection Table.
-- =============================================
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

