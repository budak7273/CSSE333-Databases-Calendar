USE [master]
GO
/****** Object:  Database [CalendarDB2]    Script Date: 5/22/2020 10:43:22 AM ******/
CREATE DATABASE [CalendarDB2]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'CalendarDB2', FILENAME = N'E:\Database\MSSQL12.MSSQLSERVER\MSSQL\DATA\CalendarDB2.mdf' , SIZE = 5120KB , MAXSIZE = UNLIMITED, FILEGROWTH = 10%)
 LOG ON 
( NAME = N'CalendarDB2_log', FILENAME = N'E:\Database\MSSQL12.MSSQLSERVER\MSSQL\DATA\CalendarDB2_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [CalendarDB2] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [CalendarDB2].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [CalendarDB2] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [CalendarDB2] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [CalendarDB2] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [CalendarDB2] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [CalendarDB2] SET ARITHABORT OFF 
GO
ALTER DATABASE [CalendarDB2] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [CalendarDB2] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [CalendarDB2] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [CalendarDB2] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [CalendarDB2] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [CalendarDB2] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [CalendarDB2] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [CalendarDB2] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [CalendarDB2] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [CalendarDB2] SET  DISABLE_BROKER 
GO
ALTER DATABASE [CalendarDB2] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [CalendarDB2] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
/*ALTER DATABASE [CalendarDB2] SET TRUSTWORTHY OFF 
GO*/
ALTER DATABASE [CalendarDB2] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [CalendarDB2] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [CalendarDB2] SET READ_COMMITTED_SNAPSHOT OFF 
GO
/*ALTER DATABASE [CalendarDB2] SET HONOR_BROKER_PRIORITY OFF 
GO*/
ALTER DATABASE [CalendarDB2] SET RECOVERY FULL 
GO
ALTER DATABASE [CalendarDB2] SET  MULTI_USER 
GO
ALTER DATABASE [CalendarDB2] SET PAGE_VERIFY CHECKSUM  
GO
/*ALTER DATABASE [CalendarDB2] SET DB_CHAINING OFF 
GO*/
ALTER DATABASE [CalendarDB2] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [CalendarDB2] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [CalendarDB2] SET DELAYED_DURABILITY = DISABLED 
GO
USE [CalendarDB2]
GO
-- /****** Object:  User [SodaBaseUserhamilter30]    Script Date: 5/22/2020 10:43:23 AM ******/
--CREATE USER [SodaBaseUserhamilter30] FOR LOGIN [SodaBaseUserhamilter30] WITH DEFAULT_SCHEMA=[dbo]
--GO
--/****** Object:  User [nandoltw]    Script Date: 5/22/2020 10:43:23 AM ******/
--CREATE USER [nandoltw] FOR LOGIN [nandoltw] WITH DEFAULT_SCHEMA=[dbo]
--GO
--/****** Object:  User [budakrc]    Script Date: 5/22/2020 10:43:23 AM ******/
--CREATE USER [budakrc] FOR LOGIN [budakrc] WITH DEFAULT_SCHEMA=[dbo]
--GO
--/****** Object:  User [appUserCalendarDB]    Script Date: 5/22/2020 10:43:23 AM ******/
--CREATE USER [appUserCalendarDB] FOR LOGIN [appUserCalendarDB] WITH DEFAULT_SCHEMA=[dbo]
--GO
--/****** Object:  DatabaseRole [db_executor]    Script Date: 5/22/2020 10:43:23 AM ******/
CREATE ROLE [db_executor]
GO
/* ALTER ROLE [db_datareader] ADD MEMBER [SodaBaseUserhamilter30]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [SodaBaseUserhamilter30]
GO
ALTER ROLE [db_owner] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_accessadmin] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_securityadmin] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_ddladmin] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_backupoperator] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_datareader] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [nandoltw]
GO
ALTER ROLE [db_owner] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_accessadmin] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_securityadmin] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_ddladmin] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_backupoperator] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_datareader] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [budakrc]
GO
ALTER ROLE [db_executor] ADD MEMBER [appUserCalendarDB]
GO
ALTER ROLE [db_datareader] ADD MEMBER [appUserCalendarDB]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [appUserCalendarDB]
GO */
/****** Object:  Table [dbo].[Assignment]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Assignment](
	[AssignmentID] [int] IDENTITY(1,1) NOT NULL,
	[EventName] [nvarchar](75) NULL,
	[EventDate] [datetime] NOT NULL,
	[EventProgress] [tinyint] NULL,
	[Type] [nvarchar](20) NULL,
	[EventSpecificColor] [int] NULL,
	[ParentClassCalendarID] [int] NOT NULL,
	[ParentClassSectionID] [int] NULL,
	[ImportSourceID] [int] NULL,
	[EventDescription] [nvarchar](257) NULL,
 CONSTRAINT [PK_Assignment] PRIMARY KEY CLUSTERED 
(
	[AssignmentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ClassCalendar]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ClassCalendar](
	[ClassCalendarID] [int] IDENTITY(1,1) NOT NULL,
	[CalendarColor] [int] NULL,
	[ClassTime] [time](7) NOT NULL,
	[ClassName] [nvarchar](20) NOT NULL,
	[ParentUserID] [varchar](20) NOT NULL,
	[ClassSectionID] [int] NULL,
 CONSTRAINT [PK_ClassCalendar] PRIMARY KEY CLUSTERED 
(
	[ClassCalendarID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ClassSection]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ClassSection](
	[ClassSectionID] [int] IDENTITY(1,1) NOT NULL,
	[CourseNumber] [varchar](8) NOT NULL,
	[SectionNumber] [tinyint] NOT NULL,
	[ClassName] [nvarchar](20) NOT NULL,
	[IsPrivate] [bit] NOT NULL,
	[SchoolYear] [smallint] NOT NULL,
	[ParentUserID] [varchar](20) NOT NULL,
	[ClassCalendarID] [int] NULL,
 CONSTRAINT [PK_ClassSection] PRIMARY KEY CLUSTERED 
(
	[ClassSectionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImportSource]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImportSource](
	[ImportSourceID] [int] IDENTITY(1,1) NOT NULL,
	[Type] [smallint] NOT NULL,
	[ParentCalendarID] [int] NOT NULL,
 CONSTRAINT [PK_ImportSource] PRIMARY KEY CLUSTERED 
(
	[ImportSourceID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NotificationMethod]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NotificationMethod](
	[NotificationMethodID] [int] IDENTITY(1,1) NOT NULL,
	[Type] [smallint] NOT NULL,
	[ParentCalendarID] [int] NOT NULL,
 CONSTRAINT [PK_NotificationMethod] PRIMARY KEY CLUSTERED 
(
	[NotificationMethodID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[Username] [varchar](20) NOT NULL,
	[Name] [nvarchar](30) NOT NULL,
	[Salt] [varchar](50) NOT NULL,
	[Hash] [varchar](50) NOT NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserCalendarSharing]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserCalendarSharing](
	[Username] [varchar](20) NOT NULL,
	[ClassCalendarID] [int] NOT NULL,
 CONSTRAINT [PK_UserCalendarSharing] PRIMARY KEY CLUSTERED 
(
	[Username] ASC,
	[ClassCalendarID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Index [Assignment_By_ClassCalendarID]    Script Date: 5/22/2020 10:43:23 AM ******/
CREATE NONCLUSTERED INDEX [Assignment_By_ClassCalendarID] ON [dbo].[Assignment]
(
	[ParentClassCalendarID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [Assignment_By_ClassSection]    Script Date: 5/22/2020 10:43:23 AM ******/
CREATE NONCLUSTERED INDEX [Assignment_By_ClassSection] ON [dbo].[Assignment]
(
	[ParentClassSectionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [ClassCalendar_By_ParentUserID]    Script Date: 5/22/2020 10:43:23 AM ******/
CREATE NONCLUSTERED INDEX [ClassCalendar_By_ParentUserID] ON [dbo].[ClassCalendar]
(
	[ParentUserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Assignment]  WITH CHECK ADD  CONSTRAINT [FK_Assignment-ClassCalendar_ParentClassCalendarID] FOREIGN KEY([ParentClassCalendarID])
REFERENCES [dbo].[ClassCalendar] ([ClassCalendarID])
GO
ALTER TABLE [dbo].[Assignment] CHECK CONSTRAINT [FK_Assignment-ClassCalendar_ParentClassCalendarID]
GO
ALTER TABLE [dbo].[Assignment]  WITH CHECK ADD  CONSTRAINT [FK_Assignment-ClassSection_ParentClassSectionID] FOREIGN KEY([ParentClassSectionID])
REFERENCES [dbo].[ClassSection] ([ClassSectionID])
GO
ALTER TABLE [dbo].[Assignment] CHECK CONSTRAINT [FK_Assignment-ClassSection_ParentClassSectionID]
GO
ALTER TABLE [dbo].[Assignment]  WITH CHECK ADD  CONSTRAINT [FK_Assignment-ImportSource_ImportSourceID] FOREIGN KEY([ImportSourceID])
REFERENCES [dbo].[ImportSource] ([ImportSourceID])
GO
ALTER TABLE [dbo].[Assignment] CHECK CONSTRAINT [FK_Assignment-ImportSource_ImportSourceID]
GO
ALTER TABLE [dbo].[ClassCalendar]  WITH CHECK ADD FOREIGN KEY([ClassSectionID])
REFERENCES [dbo].[ClassSection] ([ClassSectionID])
GO
ALTER TABLE [dbo].[ClassCalendar]  WITH CHECK ADD  CONSTRAINT [FK_ClassCalendar-User_ParentUserID] FOREIGN KEY([ParentUserID])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[ClassCalendar] CHECK CONSTRAINT [FK_ClassCalendar-User_ParentUserID]
GO
ALTER TABLE [dbo].[ClassSection]  WITH CHECK ADD FOREIGN KEY([ClassCalendarID])
REFERENCES [dbo].[ClassCalendar] ([ClassCalendarID])
GO
ALTER TABLE [dbo].[ClassSection]  WITH CHECK ADD  CONSTRAINT [FK_ClassSection-User_ParentUserID] FOREIGN KEY([ParentUserID])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[ClassSection] CHECK CONSTRAINT [FK_ClassSection-User_ParentUserID]
GO
ALTER TABLE [dbo].[ImportSource]  WITH CHECK ADD  CONSTRAINT [FK_ImportSource-ClassCalendar_ParentCalendarID] FOREIGN KEY([ParentCalendarID])
REFERENCES [dbo].[ClassCalendar] ([ClassCalendarID])
GO
ALTER TABLE [dbo].[ImportSource] CHECK CONSTRAINT [FK_ImportSource-ClassCalendar_ParentCalendarID]
GO
ALTER TABLE [dbo].[NotificationMethod]  WITH CHECK ADD  CONSTRAINT [FK_NotificationMethod-ClassCalendar_ParentCalendarID] FOREIGN KEY([ParentCalendarID])
REFERENCES [dbo].[ClassCalendar] ([ClassCalendarID])
GO
ALTER TABLE [dbo].[NotificationMethod] CHECK CONSTRAINT [FK_NotificationMethod-ClassCalendar_ParentCalendarID]
GO
ALTER TABLE [dbo].[UserCalendarSharing]  WITH CHECK ADD  CONSTRAINT [FK_UserCalendarSharing_ClassCalendar] FOREIGN KEY([ClassCalendarID])
REFERENCES [dbo].[ClassCalendar] ([ClassCalendarID])
GO
ALTER TABLE [dbo].[UserCalendarSharing] CHECK CONSTRAINT [FK_UserCalendarSharing_ClassCalendar]
GO
ALTER TABLE [dbo].[UserCalendarSharing]  WITH CHECK ADD  CONSTRAINT [FK_UserCalendarSharing_User] FOREIGN KEY([Username])
REFERENCES [dbo].[User] ([Username])
GO
ALTER TABLE [dbo].[UserCalendarSharing] CHECK CONSTRAINT [FK_UserCalendarSharing_User]
GO
ALTER TABLE [dbo].[Assignment]  WITH CHECK ADD  CONSTRAINT [CK_Assignment_GTEqual0] CHECK  (([EventProgress]>=(0)))
GO
ALTER TABLE [dbo].[Assignment] CHECK CONSTRAINT [CK_Assignment_GTEqual0]
GO
ALTER TABLE [dbo].[Assignment]  WITH CHECK ADD  CONSTRAINT [CK_Assignment_LTEqual100] CHECK  (([EventProgress]<=(100)))
GO
ALTER TABLE [dbo].[Assignment] CHECK CONSTRAINT [CK_Assignment_LTEqual100]
GO
ALTER TABLE [dbo].[ClassCalendar]  WITH CHECK ADD  CONSTRAINT [CK_ClassCalendar_ColorGTEqual0] CHECK  (([CalendarColor]>=(0)))
GO
ALTER TABLE [dbo].[ClassCalendar] CHECK CONSTRAINT [CK_ClassCalendar_ColorGTEqual0]
GO
ALTER TABLE [dbo].[ClassSection]  WITH CHECK ADD  CONSTRAINT [CK_ClassSection_SchoolYearGreaterThanZero] CHECK  (([SchoolYear]>(0)))
GO
ALTER TABLE [dbo].[ClassSection] CHECK CONSTRAINT [CK_ClassSection_SchoolYearGreaterThanZero]
GO
ALTER TABLE [dbo].[ClassSection]  WITH CHECK ADD  CONSTRAINT [CK_ClassSection_SectionNumberGreaterThan0] CHECK  (([SectionNumber]>(0)))
GO
ALTER TABLE [dbo].[ClassSection] CHECK CONSTRAINT [CK_ClassSection_SectionNumberGreaterThan0]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [CK_User_MinNameLength4Chars] CHECK  ((len([Name])>=(4)))
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [CK_User_MinNameLength4Chars]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [CK_User_MinUsernameLength1Character] CHECK  ((len([Username])>(0)))
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [CK_User_MinUsernameLength1Character]
GO
/****** Object:  StoredProcedure [dbo].[delete_Assignment]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[delete_Assignment](
@AssignmentID_1 int
)
AS
IF NOT EXISTS 
(
SELECT AssignmentID
FROM Assignment
WHERE AssignmentID = @AssignmentID_1)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred deleting the class section information.';  
	RETURN 99;  
END
ELSE 
	DELETE FROM Assignment
	WHERE AssignmentID = @AssignmentID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class section has been deleted.';  
        RETURN 0;  
    END;  
GO
/****** Object:  StoredProcedure [dbo].[delete_ClassCalendar]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[delete_ClassCalendar](
@ClassCalendarID_1 int
)
AS
IF NOT EXISTS 
(
SELECT ClassCalendarID
FROM ClassCalendar
WHERE ClassCalendarID = @ClassCalendarID_1)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred deleting the class calendar information.';  
	RETURN 99;  
END
ELSE 
	DELETE FROM ClassCalendar
	WHERE ClassCalendarID = @ClassCalendarID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class calendar has been deleted.';  
        RETURN 0;  
    END;  

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [delete_ClassCalendar] TO appUserCalendarDB;

GO
/****** Object:  StoredProcedure [dbo].[delete_ClassSection]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[delete_ClassSection](
@ClassSectionID_1 int
)
AS
IF NOT EXISTS 
(
SELECT ClassSectionID
FROM ClassSection
WHERE ClassSectionID = @ClassSectionID_1)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred deleting the class section information.';  
	RETURN 99;  
END
ELSE 
	DELETE FROM ClassSection
	WHERE ClassSectionID = @ClassSectionID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class section has been deleted.';  
        RETURN 0;  
    END;  
GO
/****** Object:  StoredProcedure [dbo].[delete_UserCalendarSharing_Relationship]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[delete_UserCalendarSharing_Relationship]
(@Username_1	[varchar](20),
 @CalendarID_2	[int])
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	IF @CalendarID_2 IS NULL
	BEGIN
		PRINT N'CalendarID can not be null'
		RETURN 2
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 3
	END

	-- Check to see if Calendar ID is valid
	IF (SELECT Count(ClassCalendarID) from [ClassCalendar] WHERE ClassCalendarID = @CalendarID_2) != 1
	BEGIN
		PRINT N'That ClassCalendarID does not exist in the database'
		RETURN 4
	END

	--Actual Procedure--

	DELETE FROM [UserCalendarSharing]
	WHERE Username = @Username_1 AND ClassCalendarID = @CalendarID_2

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred removing the followed calendar for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The followed calendar was removed successfully for the user, if it exists'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[get_All_Assignments_In_ClassCalendar]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[get_All_Assignments_In_ClassCalendar]
(@ParentClassCalendarID_1	[int])
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @ParentClassCalendarID_1 IS NULL
	BEGIN
		RAISERROR('ParentClassCalendarID can not be null', 14, 1)
		RETURN 1
	END

	-- Check to see if ParentClassCalendarID is valid
	IF (SELECT Count(ClassCalendarID) from ClassCalendar WHERE ClassCalendarID = @ParentClassCalendarID_1) != 1
	BEGIN
		RAISERROR('The ClassCalendarID does not exist in the database', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	SELECT *
	FROM [Assignment] asn
	WHERE asn.ParentClassCalendarID = @ParentClassCalendarID_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the assignments.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignments were fetched successfully.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[get_All_Assignments_Of_User]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[get_All_Assignments_Of_User]
(@Username_1	[varchar](20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 2
	END

	--Actual Procedure--

	SELECT *
	FROM [Assignment] asn
		INNER JOIN [ClassCalendar] ccal
		ON asn.ParentClassCalendarID = ccal.ClassCalendarID
		LEFT JOIN [UserCalendarSharing] ucalShare --left so unshared calendars still show up
		ON ucalShare.ClassCalendarID = ccal.ClassCalendarID
	WHERE ccal.ParentUserID = @Username_1 OR ucalShare.Username = @Username_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred getting the assignments for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignments were fetched successfully for the user.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[get_All_Class_Sections]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
CREATE PROCEDURE [dbo].[get_All_Class_Sections] (@Username_1 varchar(20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--

	SELECT *
	FROM ClassSection

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
/****** Object:  StoredProcedure [dbo].[get_All_Followed_Calendars_Of_User]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[get_All_Followed_Calendars_Of_User]
(@Username_1	[varchar](20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 2
	END

	--Actual Procedure--

	SELECT ccal.ClassCalendarID as [CalendarID], ccal.ClassName as [Name], ccal.ParentUserID as [Creator]
	FROM [UserCalendarSharing] calShare
		INNER JOIN [ClassCalendar] ccal
		ON ccal.ClassCalendarID = calShare.ClassCalendarID
	WHERE calShare.Username = @Username_1

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred getting the followed calendars for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The followed calendars were fetched successfully for the user.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[get_ClassCalendar_List]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[get_ClassCalendar_List]
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--

	SELECT ccal.ClassCalendarID as [CalendarID], ccal.ClassName as [Name], ccal.ParentUserID as [Creator]
	FROM [ClassCalendar] ccal

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the class calendar list.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The class calendar list was fetched successfully.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[get_Not_Subscribed_Class_Sections]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
CREATE PROCEDURE [dbo].[get_Not_Subscribed_Class_Sections] (@Username_1 varchar(20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--
	
	SELECT cs.*--, cc.ClassCalendarID AS SubcalendarID
		FROM ClassSection cs
			INNER JOIN ClassCalendar cc
				ON cs.ClassSectionID = cc.ClassSectionID
					WHERE cc.ClassCalendarID IS NULL AND cc.ParentUserID = @Username_1	
	


	

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
/****** Object:  StoredProcedure [dbo].[get_Owned_Class_Sections]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
CREATE PROCEDURE [dbo].[get_Owned_Class_Sections] (@Username_1 varchar(20))
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
/****** Object:  StoredProcedure [dbo].[get_Salt_for_User]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[get_Salt_for_User]
	@Username_1 nvarchar(20)
AS 
	SELECT [Salt] 
		FROM [User] 
		WHERE Username = @Username_1

--	IF (SELECT COUNT(*) FROM [User] WHERE Username = @Username_1) != 1
--		RETURN 1;
--	ELSE
--		RETURN 0;
GO
/****** Object:  StoredProcedure [dbo].[get_Subscribed_Class_Sections]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
CREATE PROCEDURE [dbo].[get_Subscribed_Class_Sections] (@Username_1 varchar(20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Actual Procedure--

	SELECT cs.*, cc.ClassCalendarID AS SubcalendarID
	FROM ClassSection cs
		INNER JOIN ClassCalendar cc
			ON cs.ClassSectionID = cc.ClassSectionID
	WHERE /*cc.ClassCalendarID IS NOT NULL AND */cc.ParentUserID = @Username_1

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
/****** Object:  StoredProcedure [dbo].[get_user_ClassCalendar_List]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[get_user_ClassCalendar_List]
(@Username_1	[varchar](20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 2
	END

	--Actual Procedure--

	SELECT DISTINCT ccal.ClassCalendarID as [CalendarID], ccal.ClassName as [Name], ccal.ParentUserID as [Creator]
	FROM [ClassCalendar] ccal
		LEFT JOIN [UserCalendarSharing] ucalShare
		ON ccal.ClassCalendarID = ucalShare.ClassCalendarID
	WHERE ccal.ParentUserID = @Username_1 OR ucalShare.Username = @Username_1
	ORDER BY ccal.ClassCalendarID

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred getting the class calendar list.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The class calendar list was fetched successfully.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[insert_Assignment]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[insert_Assignment]
(@EventName_1				[nvarchar](75) = null,
 @EventDate_2				[datetime],
 @EventProgress_3			[tinyint] = 0,
 @Type_4					[nvarchar](20) = null,
 @EventSpecificColor_5		[int] = null,
 @ParentClassCalendarID_6	[int],
 @ParentClassSectionID_7	[int] = null,
 @ImportSourceID_8			[int] = null,
 @EventDescription_9		[varchar](257) = null)
AS
	-- Suppress row count messages
	SET NOCOUNT ON

	-- For debugging datetime recieved
	/*DECLARE @a nvarchar(20) = CAST(@EventDate_2 AS VARCHAR(20))
	RAISERROR('Date on import is %s', 18, 1, @a)
	*/

	--Check parameters that aren't allowed to be null
	IF @EventDate_2 IS NULL
	BEGIN
		PRINT N'EventDate can not be null'
		RETURN 1
	END

	IF @ParentClassCalendarID_6 IS NULL
	BEGIN
		PRINT N'ParentClassCalendarID can not be null'
		RETURN 1
	END

	--Check max length on EventName
	IF LEN(@EventName_1) > 75
	BEGIN
		PRINT N'EventName above maximum of 75 characters'
		RETURN 1
	END

	--Check max length on Type
	IF LEN(@Type_4) > 20
	BEGIN
		PRINT N'Type above maximum of 20 characters'
		RETURN 1
	END

	--Check max length on EventDescription
	IF LEN(@EventDescription_9) > 257
	BEGIN
		PRINT N'EventDescription above maximum of 257 characters'
		RETURN 1
	END

	--Set @ParentClassSectionID_7 to null class section if -1
	IF @ParentClassSectionID_7 = -1
	BEGIN
		SET @ParentClassSectionID_7 = null
	END

	--If @EventProgress_3 is given as null, set it to 0
	IF @EventProgress_3 IS NULL
	BEGIN
		SET @EventProgress_3 = 0
	END
	--Check bounds on progress
	ELSE IF @EventProgress_3 < 0 OR @EventProgress_3 > 100
	BEGIN
		PRINT N'EventProgress outside of bounds: 0 to 100 inclusive'
		RETURN 1
	END

	--Actual Procedure--

	INSERT INTO [Assignment]
		       ([EventName],	[EventDate],	[EventProgress],	[Type],		[EventSpecificColor],	[ParentClassCalendarID],	[ParentClassSectionID],		[ImportSourceID],	[EventDescription])
		VALUES (@EventName_1,	@EventDate_2,	@EventProgress_3,	@Type_4,	@EventSpecificColor_5,	@ParentClassCalendarID_6,	@ParentClassSectionID_7,	@ImportSourceID_8,	@EventDescription_9)

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred inserting the assignment.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The assignment was inserted successfully.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[insert_ClassCalendar]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[insert_ClassCalendar](
	@CalendarColor_1 int,
	@ClassTime_2 time,
	@ClassName_3 nvarchar(20),
	@ParentUserID_4 varchar(20)
)
AS

	-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'

	--Check for ParentUserID
	IF NOT EXISTS 
	(
	SELECT ParentUserID
	FROM ClassCalendar
	WHERE 
	ParentUserID = @ParentUserID_4
	)
	BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred, ParentUserID does not exist.';  
	RETURN 99;  
	END

	--Check paramters that aren't allowed to be null
	IF @CalendarColor_1 IS NULL
	BEGIN
		SET @CalendarColor_1 = 333333
	END

	IF @ClassTime_2 is NULL
	BEGIN
		PRINT('ClassTime cannot be null')
		RETURN 2
	END

	IF @ClassName_3 is NULL
	BEGIN
		PRINT('ClassName cannot be null')
		RETURN 3
	END

	IF @ParentUserID_4 is NULL
	BEGIN
		PRINT('ParentUserID cannot be null')
		RETURN 4
	END

	--Check if CalendarColor is positive int
	IF @CalendarColor_1 < 0
	BEGIN
		PRINT('CalendarColor must be positive integer of 0 or greater')
		RETURN 5
	END

	--Check max length on ClassName
	IF LEN(@ClassName_3) > 20
	BEGIN
		PRINT('ClassName above maximum of 20 characters')
		RETURN 6
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_4) > 20
	BEGIN
		PRINT('ParentUserID above maximum of 20 characters')
		RETURN 7
	END

	--Actual Procedure--

	BEGIN
		INSERT INTO ClassCalendar(
		CalendarColor,
		ClassTime,
		ClassName,
		ParentUserID)

		VALUES(
		@CalendarColor_1,
		@ClassTime_2,
		@ClassName_3,
		@ParentUserID_4)
	END

	--End Actual Procedure

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT('An error occurred inserting the Class Calendar.')
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The Class Calendar was inserted successfully.'
		RETURN 0
	END
	
GO
/****** Object:  StoredProcedure [dbo].[insert_ClassSection]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
CREATE PROCEDURE [dbo].[insert_ClassSection](
@CourseNumber_1 varchar(8), 
@SectionNumber_2 tinyint,
@ClassName_3 nvarchar(20), 
@IsPrivate_4 bit,
@SchoolYear_5 smallint, 
@ParentUserID_6 varchar(20),
@ClassCalenddarID_7 int
)
AS

	-- Supress row count messages
	SET NOCOUNT ON
	PRINT 'DEBUG start'
	--Check for ParentUserID
	IF NOT EXISTS 
	(
	SELECT Username
	FROM [User]
	WHERE 
	Username = @ParentUserID_6
	)
	BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred, ParentUserID does not exist.';  
	RETURN 99;  
	END

	--Check parameters that aren't allowed to be null
	IF @CourseNumber_1 is NULL
	BEGIN
		PRINT('Course Number cannot be null')
		RETURN 1
	END

	IF @SectionNumber_2 is NULL
	BEGIN
		PRINT('Section Number cannot be null')
		RETURN 2
	END

	IF @ClassName_3 is NULL
	BEGIN
		PRINT('Class Name cannot be null')
		RETURN 3
	END

	IF @IsPrivate_4 is NULL
	BEGIN
		PRINT('IsPrivate cannot be null')
		RETURN 4
	END

	IF @SchoolYear_5 is NULL
	BEGIN
		PRINT('School Year cannot be null')
		RETURN 5
	END

	IF @ParentUserID_6 is NULL
	BEGIN
		PRINT('ParentUserID cannot be null')
		RETURN 6
	END

	--Check max length on CourseNumber
	IF LEN(@CourseNumber_1) > 8
	BEGIN
		PRINT('Course Number above maximum of 20 characters')
		RETURN 8
	END

	--Check max length on ClassName
	IF LEN(@ClassName_3) > 20
	BEGIN
		PRINT('Class Name above maximum of 20 characters')
		RETURN 9
	END

	--Check max length on ParentUserID
	IF LEN(@ParentUserID_6) > 20
	BEGIN
		PRINT('ParentUserID above maximum of 20 characters')
		RETURN 10
	END

	IF @ClassCalenddarID_7 is NULL
	BEGIN
		PRINT('ClassCalendarID cannot be null')
		RETURN 7
	END

	--Actual Procedure--

	BEGIN

		INSERT INTO ClassSection
		(CourseNumber, SectionNumber, ClassName, IsPrivate, SchoolYear, ParentUserID, ClassCalendarID)
		Values(@CourseNumber_1, @SectionNumber_2, @ClassName_3, @IsPrivate_4, @SchoolYear_5, @ParentUserID_6, @ClassCalenddarID_7)
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

GO
/****** Object:  StoredProcedure [dbo].[insert_UserCalendarSharing_Relationship]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[insert_UserCalendarSharing_Relationship]
(@Username_1	[varchar](20),
 @CalendarID_2	[int])
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		RAISERROR('Username can not be null', 14, 1)
		RETURN 1
	END

	IF @CalendarID_2 IS NULL
	BEGIN
		RAISERROR('CalendarID can not be null', 14, 1)
		RETURN 1
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		RAISERROR('That Username does not exist in the database', 14, 1)
		RETURN 1
	END

	-- Check to see if Calendar ID is valid
	IF (SELECT Count(ClassCalendarID) from [ClassCalendar] WHERE ClassCalendarID = @CalendarID_2) != 1
	BEGIN
		RAISERROR('That ClassCalendarID does not exist in the database', 14, 1)
		RETURN 1
	END

	--Actual Procedure--

	INSERT INTO [UserCalendarSharing]
		       ([Username],		[ClassCalendarID])
		VALUES (@Username_1,	@CalendarID_2)

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		RAISERROR('An error occurred getting the followed calendars for the user.', 14, @Status)
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The followed calendars were fetched successfully for the user.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[join_Class_Section]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[join_Class_Section](
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
GO
/****** Object:  StoredProcedure [dbo].[leave_Class_Section]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- Create procedure
CREATE PROCEDURE [dbo].[leave_Class_Section] (@ClassCalendarID int)
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
/****** Object:  StoredProcedure [dbo].[prepareImport]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[prepareImport]
(@Username_1	[varchar](20),
 @ClassName_2	[varchar](20))
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	IF @ClassName_2 IS NULL OR LEN(@ClassName_2) = 0
	BEGIN
		PRINT N'Name can not be null or empty'
		RETURN 2
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 3
	END

	--Actual Procedure--

	--DECLARE @NewCalendarIDTable table([NewId] INT)
	DECLARE @NewCalendarID INT
	DECLARE @NewImportSourceID INT

	BEGIN TRANSACTION
	--Create the new ClassCalendar to be imported into
	INSERT INTO [ClassCalendar]
	--OUTPUT inserted.ClassCalendarID INTO @NewCalendarIDTable
		       ([CalendarColor],	[ClassTime],	[ClassName],	[ParentUserID])
		VALUES (0,					{t '12:00:00'},	@ClassName_2,	@Username_1)
	
	SET @NewCalendarID = SCOPE_IDENTITY()
	--PRINT 'New calendar ID is'
	--PRINT @NewCalendarID

	--Create the new ImportSource
	INSERT INTO [ImportSource]
		       ([Type],	[ParentCalendarID])
		VALUES (1,		@NewCalendarID)

	SET @NewImportSourceID = SCOPE_IDENTITY()
	--PRINT 'New ImportSource ID is'
	--PRINT @NewImportSourceID

	--return values of the SPROC
	SELECT @NewCalendarID AS NewCalendarID, @NewImportSourceID AS NewImportSourceID
	COMMIT TRANSACTION

	--End Actual Procedure--

	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occurred getting the followed calendars for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT 'The followed calendars were fetched successfully for the user.'
		RETURN 0
	END
GO
/****** Object:  StoredProcedure [dbo].[register_User]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[register_User](
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
GO
/****** Object:  StoredProcedure [dbo].[update_ClassCalendar]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[update_ClassCalendar](
@ClassCalendarID_1 int,
@CalendarColor_1 int,
@ClassTime_2 time,
@ClassName_3 nvarchar(20),
@ParentUserID_4 varchar(20)
)
AS
IF NOT EXISTS 
(
SELECT ClassCalendarID
FROM ClassCalendar
WHERE 
ClassCalendarID = @ClassCalendarID_1
)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred updating the class calendar information.';  
	RETURN 99;  
END
ELSE
	UPDATE ClassCalendar
	SET

	CalendarColor = @CalendarColor_1,
	ClassTime = @ClassTime_2,
	ClassName = @ClassName_3,
	ParentUserID = @ParentUserID_4
	
	WHERE ClassCalendarID = @ClassCalendarID_1
	BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class calendar has been updated.';  
        RETURN 0;  
    END; 
GO
/****** Object:  StoredProcedure [dbo].[update_ClassSection]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[update_ClassSection] (
@ClassSectionID_1 int,
@CourseNumber_1 varchar(8), 
@SectionNumber_2 tinyint,
@ClassName_3 nvarchar(20), 
@IsPrivate_4 bit, 
@SchoolYear_5 smallint, 
@ParentUserID_6 varchar(20)
)
AS
IF NOT EXISTS 
(
SELECT ClassSectionID
FROM ClassSection
WHERE 
ClassSectionID = @ClassSectionID_1
)

BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred updating the class section information.';  
	RETURN 99;  
END
ELSE 
	UPDATE ClassSection
	SET
	CourseNumber = @CourseNumber_1,
	SectionNumber = @SectionNumber_2,
	ClassName = @ClassName_3,
	IsPrivate = @IsPrivate_4,
	SchoolYear = @SchoolYear_5,
	ParentUserID = @ParentUserID_6

	WHERE ClassSectionID = @ClassSectionID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class section has been updated.';  
        RETURN 0;  
    END;  
GO
/****** Object:  StoredProcedure [dbo].[update_User_Password]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[update_User_Password]
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
		PRINT N'Username can not be null'
		RETURN 1
	END

	IF @OldPasswordHash_2 IS NULL
	BEGIN
		PRINT N'OldPasswordHash can not be null'
		RETURN 2
	END

	IF @NewPasswordHash_3 IS NULL
	BEGIN
		PRINT N'NewPasswordHash can not be null'
		RETURN 3
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 4
	END

	-- Check to see if current hash matches OldPasswordHash, error if not (which is why this isn't just part of the WHERE clause of the UPDATE)
	IF (SELECT TOP 1 usr.[Hash] FROM [User] usr WHERE [Username] = @Username_1) <> @OldPasswordHash_2
	BEGIN
		PRINT N'The old password did not match the new password'
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
		PRINT N'An error occured while updating the password for the user.'
		RETURN @Status
	END
	ELSE
	BEGIN
		-- Return 0 to the calling program to indicate success.
		PRINT N'The user password was updated successfully.'
		RETURN 0
	END


-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [update_User_Password] TO appUserCalendarDB;
GO
/****** Object:  StoredProcedure [dbo].[verify_Hash_for_User]    Script Date: 5/22/2020 10:43:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Create procedure
CREATE PROCEDURE [dbo].[verify_Hash_for_User]
(@Username_1		[varchar](20),
 @PasswordHash_2	[varchar](50)
 )
AS
	-- Supress row count messages
	SET NOCOUNT ON

	--Check parameters that aren't allowed to be null
	IF @Username_1 IS NULL
	BEGIN
		PRINT N'Username can not be null'
		RETURN 1
	END

	IF @PasswordHash_2 IS NULL
	BEGIN
		PRINT N'PasswordHash can not be null'
		RETURN 2
	END

	-- Check to see if Username is valid
	IF (SELECT Count(Username) from [User] WHERE Username = @Username_1) != 1
	BEGIN
		PRINT N'That Username does not exist in the database'
		RETURN 3
	END

	--Actual Procedure--

	-- Check to see if current hash matches OldPasswordHash, error if not (which is why this isn't just part of the WHERE clause of the UPDATE)
	IF (SELECT TOP 1 usr.[Hash] FROM [User] usr WHERE [Username] = @Username_1) <> @PasswordHash_2
	BEGIN
		PRINT N'The password did not match'
		RETURN 4
	END

	--End Actual Procedure--


	-- Check for errors
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status <> 0 
	BEGIN
		-- Return error code to the calling program to indicate failure.
		PRINT N'An error occured verifying the password for the user.'
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
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'A username can''t be a blank string' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'User', @level2type=N'CONSTRAINT',@level2name=N'CK_User_MinUsernameLength1Character'
GO
USE [master]
GO
ALTER DATABASE [CalendarDB2] SET  READ_WRITE 
GO

USE CalendarDB2
GO

CREATE USER [appUserCalendarDB] FROM LOGIN [appUserCalendarDB];
exec sp_addrolemember 'db_owner', 'appUserCalendarDB';
GO

ALTER ROLE [db_executor] ADD MEMBER [appUserCalendarDB]
GO
ALTER ROLE [db_datareader] ADD MEMBER [appUserCalendarDB]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [appUserCalendarDB]
GO

-- =============================================
-- Author:		Thomas Nandola
-- Create date: May 6th, 2020
-- Description:	Procedure to Delete from the ClassSection Table.
-- =============================================
/*
EXEC delete_ClassSection 7
select * from ClassSection
*/

USE CalendarDB
GO

-- Drop procedure if already exists
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'delete_ClassSection') --got this method of checking if a proc exists from stackoverflow q#2072086
	DROP PROCEDURE [delete_ClassSection]
GO

CREATE PROCEDURE delete_ClassSection(
@ClassSectionID_1 int
)
AS
IF NOT EXISTS 
(
SELECT ClassSectionID
FROM ClassSection
WHERE ClassSectionID = @ClassSectionID_1)
BEGIN  
	-- Return 99 to the calling program to indicate failure.  
	PRINT N'An error occurred deleting the class section information.';  
	RETURN 99;  
END
ELSE 
	DELETE FROM ClassSection
	WHERE ClassSectionID = @ClassSectionID_1
    BEGIN  
        -- Return 0 to the calling program to indicate success.  
        PRINT N'The class section has been deleted.';  
        RETURN 0;  
    END;  
GO

-- Grant usage to the app user (needs to happen again when it gets deleted and re-created)
GRANT EXECUTE ON [delete_ClassSection] TO appUserCalendarDB;