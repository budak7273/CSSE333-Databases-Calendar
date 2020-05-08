-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Delete from the ClassCalendar Table.
-- =============================================

EXEC delete_ClassCalendar 6
select * from ClassCalendar

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'delete_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [delete_ClassCalendar]
GO

CREATE PROCEDURE delete_ClassCalendar(
@ClassCalendarID_1 int,
@Msg nvarchar(MAX) = null OUTPUT
)
AS
BEGIN TRY
	DELETE FROM ClassCalendar
	WHERE ClassCalendarID = @ClassCalendarID_1

	SET @Msg = 'Class Calendar Deleted Successfully'

END TRY
BEGIN CATCH  
    SELECT ERROR_MESSAGE() AS ErrorMessage;  
END CATCH;  
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [delete_ClassCalendar] TO appUserCalendarDB;

