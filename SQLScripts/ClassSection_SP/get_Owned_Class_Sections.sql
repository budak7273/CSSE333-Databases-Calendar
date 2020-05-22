USE [CalendarDB]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
ALTER PROCEDURE [dbo].[get_Owned_Class_Sections] (@Username_1 varchar(20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--

	SELECT cs.*
	FROM ClassSection cs
	WHERE  ParentUserID = @Username_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the class section list.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The class section list was fetched successfully.'
		RETURN 0
	END
GO


