DECLARE @tmp DATETIME
SET @tmp = GETDATE()

EXEC [insert_Assignment] 
	@EventName_1='DemoEventName',
	@EventDate_2=@tmp,
	@ParentClassCalendarID_6=2,
	@EventProgress_3 =0