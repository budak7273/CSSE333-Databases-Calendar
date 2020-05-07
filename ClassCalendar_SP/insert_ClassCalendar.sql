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
-- Description:	Procedure to Insert into the ClassCalendar Table.
-- =============================================
CREATE PROCEDURE insert_ClassCalendar(
	@ClassCalendarID_1 int,
	@CalendarColor_1 int,
	@ClassTime_1 time,
	@ClassName_1 nvarchar(20),
	@ParentUserID_1 varchar(20))

AS
BEGIN
	INSERT INTO ClassCalendar(
	ClassCalendarID,
	CalendarColor,
	ClassTime,
	ClassName,
	ParentUserID)

	VALUES(
	@ClassCalendarID_1,
	@CalendarColor_1,
	@ClassTime_1,
	@ClassName_1,
	@ParentUserID_1)
END