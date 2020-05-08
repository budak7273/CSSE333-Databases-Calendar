-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Update the Class Calendar Table.
-- =============================================
/*
EXEC [update_ClassCalendar] 
	@ClassCalendarID_1 = 6,
	@CalendarColor_1 = 222222,
	@ClassTime_1 = '8:00',
	@ClassName_1 = 'CSSE330',
	@ParentUserID_1 = 'DemoUser'
	
select * FROM ClassCalendar
*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'update_ClassCalendar') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [update_ClassCalendar]
GO

CREATE PROCEDURE update_ClassCalendar(
@ClassCalendarID_1 int,
@CalendarColor_1 int,
@ClassTime_1 time,
@ClassName_1 nvarchar(20),
@ParentUserID_1 varchar(20),
@Msg nvarchar(MAX) = null OUTPUT
)
AS
BEGIN TRY
	UPDATE ClassCalendar
	SET

	CalendarColor = @CalendarColor_1,
	ClassTime = @ClassTime_1,
	ClassName = @ClassName_1,
	ParentUserID = @ParentUserID_1
	
	WHERE ClassCalendarID = @ClassCalendarID_1
		SET @Msg = 'Class Calendar Updated Successfully!'

END TRY
BEGIN CATCH  
    SELECT ERROR_MESSAGE() AS ErrorMessage;  
END CATCH;  
GO 

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_ClassCalendar] TO appUserCalendarDB;

