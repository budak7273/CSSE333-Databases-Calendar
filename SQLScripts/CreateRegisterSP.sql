USE CalendarDB
GO

DROP PROCEDURE Register
GO

CREATE PROCEDURE [Register](
@Username_1 varchar(20),
@Name_2 nvarchar(30),
@Timezone_3 decimal(4,2),
@Password_Hash_4 binary(64)) AS
	--Parameter Errors
	IF @Username_1 IS NULL
		RETURN 1
	IF @Name_2 IS NULL
		RETURN 2
	IF @Timezone_3 IS NULL
		RETURN 3
	IF @Password_Hash_4 IS NULL
		RETURN 4
	IF EXISTS (SELECT * FROM [User] WHERE Username = @Username_1)
		RETURN 5
	
	INSERT INTO [User]([Username], [Name], [Timezone], [Password])
		VALUES(@Username_1, @Name_2, @Timezone_3, @Password_Hash_4)
	RETURN 0