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
-- Description:	Procedure to Update the Class Calendar Table.
-- =============================================
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

EXEC [update_ClassCalendar] 
	@ClassCalendarID_1 = 6,
	@CalendarColor_1 = 222222,
	@ClassTime_1 = '8:00',
	@ClassName_1 = 'CSSE330',
	@ParentUserID_1 = 'DemoUser'
	

select * FROM ClassCalendar
