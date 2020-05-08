-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Delete from the ClassSection Table.
-- =============================================

EXEC delete_ClassSection 3
select * from ClassSection

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'delete_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [delete_ClassSection]
GO

CREATE PROCEDURE delete_ClassSection(
@ClassSectionID_1 int,
@Msg nvarchar(MAX) = null OUTPUT
)
AS
BEGIN TRY
	DELETE FROM ClassSection
	WHERE ClassSectionID = @ClassSectionID_1

	SET @Msg = 'Class Section Deleted Successfully'

END TRY
BEGIN CATCH  
    SELECT ERROR_MESSAGE() AS ErrorMessage;  
END CATCH;  
GO 

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [delete_ClassSection] TO appUserCalendarDB;