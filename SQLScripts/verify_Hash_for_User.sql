/*--------------------------------------------------------------

Returns 1 if the password hash is correct for the supplied username, 0 if otherwise

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [verify_Hash_for_User] @Username_1 ='DemoUser', @PasswordHash_2 = 'brownHash'
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'verify_Hash_for_User') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [verify_Hash_for_User]
GO

-- Create procedure
CREATE PROCEDURE [verify_Hash_for_User]
(@Username_1		[varchar](20),
 @PasswordHash_2	[varchar](50)
 )
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		RAISERROR('Username can not be null', 14, 1)
		RETURN 1
	END

	IF @PasswordHash_2 IS NULL
	BEGIN
		RAISERROR('PasswordHash can not be null', 14, 1)
		RETURN 2
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		RAISERROR('That Username does not exist in the database', 14, 1)
		RETURN 3
	END

	--Actual Procedure--

	-- Check to see if current hash matches OldPasswordHash, error if not (which is why this isn't just part of the WHERE clause of the UPDATE)
	IF (SELECT TOP 1 usr.[Hash] FROM [User] usr WHERE [Username] = @Username_1) <> @PasswordHash_2
	BEGIN
		--RAISERROR('The password did not match', 14, 1)
		RETURN 4
	END

	--End Actual Procedure--


	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occured verifying the password for the user.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The password was a match for the user.'
		RETURN 0
	END


-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [verify_Hash_for_User] TO appUserCalendarDB;