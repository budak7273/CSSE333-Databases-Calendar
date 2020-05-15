/*--------------------------------------------------------------

Updates a user's password hash given the username, their old password hash, and the new one

Author: Rob Budak
-------------------------------------------
Demo: Example with minimum parameters specified
EXEC [update_User_Password] @Username_1 ='DemoUser', @OldPasswordHash_2 = 'brownHash', @NewPasswordHash_3 = 'hashBrown'
--------------------------------------------------------------*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'update_User_Password') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [update_User_Password]
GO

-- Create procedure
CREATE PROCEDURE [update_User_Password]
(@Username_1		[varchar](20),
 @OldPasswordHash_2 [varchar](50),
 @NewPasswordHash_3 [varchar](50)
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

	IF @OldPasswordHash_2 IS NULL
	BEGIN
		RAISERROR('OldPasswordHash can not be null', 14, 1)
		RETURN 2
	END

	IF @NewPasswordHash_3 IS NULL
	BEGIN
		RAISERROR('NewPasswordHash can not be null', 14, 1)
		RETURN 3
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		RAISERROR('That Username does not exist in the database', 14, 1)
		RETURN 4
	END

	-- Check to see if current hash matches OldPasswordHash, error if not (which is why this isn't just part of the WHERE clause of the UPDATE)
	IF (SELECT TOP 1 usr.[Hash] FROM [User] usr WHERE [Username] = @Username_1) <> @OldPasswordHash_2
	BEGIN
		RAISERROR('The old password did not match the new password', 14, 1)
		RETURN 5
	END

	--Actual Procedure--
	
	UPDATE [User]
	SET [Hash] = @NewPasswordHash_3
	WHERE Username = @Username_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occured while updating the password for the user.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The user password was updated successfully.'
		RETURN 0
	END


-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_User_Password] TO appUserCalendarDB;