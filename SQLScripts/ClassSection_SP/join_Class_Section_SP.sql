ALTER PROCEDURE join_Class_Section(
@Username_1 varchar(20),
@ClassSectionID_2 int
)

AS
-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'
	--Check for Username Exists
	IF NOT EXISTS 
	(
	SELECT Username
	FROM [User]
	WHERE 
	Username = @Username_1
	)
	BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred, Username does not exist.';  
	RETURN 99;  
	END

	--Check parameters that aren't allowed to be null
	IF @ClassSectionID_2 is NULL
	BEGIN
		PRINT('ClassSectionID cannot be null')
		RETURN 1
	END


	--Actual Procedure--

	BEGIN
		INSERT INTO ClassCalendar
		(ClassName, ParentUserID, ClassSectionID, ClassTime)
		Values((SELECT ClassName FROM ClassSection WHERE ClassSectionID = @ClassSectionID_2), @Username_1, @ClassSectionID_2, CAST('2000-01-01' AS DATETIME))
	END

	--End Actual Procedure

	-- Check for errors
		DECLARE @Status SMALLINT
		SET @Status = @@ERROR
		IF @Status <> 0 
		BEGIN
			-- Return error code to the calling program to indicate failure.
			PRINT 'An error occurred inserting the Class Section.'
			RETURN @Status
		END
		ELSE
		BEGIN
			-- Return 0 to the calling program to indicate success.
			PRINT 'The Class Section was inserted successfully.'
			RETURN 0
		END