USE CalendarDB
GO

DROP PROCEDURE [register_User]
GO

CREATE PROCEDURE [register_User](
	@Username_1 varchar(20),
	@Name_2 nvarchar(30),
	@Salt_3 varchar(50),
	@Hash_4 varchar(50)
) AS
	--Parameter Errors
	IF @Username_1 IS NULL
		RETURN 1
	IF @Name_2 IS NULL
		RETURN 2
	IF @Salt_3 IS NULL
		RETURN 3
	IF @Hash_4 IS NULL
		RETURN 4
	IF EXISTS (SELECT * FROM [User] WHERE Username = @Username_1)
		RETURN 5
	
	INSERT INTO [User]([Username], [Name], [Salt], [Hash])
		VALUES(@Username_1, @Name_2, @Salt_3, @Hash_4)
	RETURN 0