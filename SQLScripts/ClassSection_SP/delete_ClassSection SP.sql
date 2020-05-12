-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Delete from the ClassSection Table.
-- =============================================

EXEC delete_ClassSection 7
select * from ClassSection


USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'delete_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [delete_ClassSection]
GO

CREATE PROCEDURE delete_ClassSection(
@ClassSectionID_1 int
)
AS
IF NOT EXISTS 
(
SELECT ClassSectionID
FROM ClassSection
WHERE ClassSectionID = @ClassSectionID_1)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred deleting the class section information.';  
	RETURN 99;  
END
ELSE 
	DELETE FROM ClassSection
	WHERE ClassSectionID = @ClassSectionID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class section has been deleted.';  
        RETURN 0;  
    END;  
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [delete_ClassSection] TO appUserCalendarDB;