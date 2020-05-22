USE [CalendarDB]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
ALTER PROCEDURE [dbo].[leave_Class_Section] (@ClassCalendarID int)
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--
	
	UPDATE ClassCalendar
		SET ClassSectionID = NULL
		WHERE ClassCalendarID = @ClassCalendarID
	

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred updating class calendar.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The class calendar was updated successfully.'
		RETURN 0
	END
GO


