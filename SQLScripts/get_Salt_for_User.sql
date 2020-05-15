USE CalendarDB
GO

DROP PROCEDURE get_Salt_for_User
GO

CREATE PROCEDURE get_Salt_for_User
	@Username_1 nvarchar(20)
AS 
	IF (SELECT COUNT(*) FROM [User] WHERE Username = @Username_1) != 1
		RETURN 1;

	SELECT [Salt] 
		FROM [User] 
		WHERE Username = @Username_1
	RETURN 0;
GO