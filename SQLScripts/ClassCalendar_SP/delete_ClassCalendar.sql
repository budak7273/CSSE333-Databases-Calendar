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
-- Description:	Procedure to Delete from the ClassCalendar Table.
-- =============================================
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
