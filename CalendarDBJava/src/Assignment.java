import java.util.Date;

public class Assignment {
    private int AssignmentID;
    private String EventName;
    private Date EventDate;
    private int EventProgress;
    private String EventType;
    private int EventSpecificColor;
    private int ParentClassCalendarID;
    private int ParentClassSectionID;
    private int ImportSourceID;

    public Assignment(int assignmentID, String eventName, Date eventDate, int eventProgress, String eventType, int eventSpecificColor, int parentClassCalendarID, int parentClassSectionID, int importSourceID) {
        AssignmentID = assignmentID;
        EventName = eventName;
        EventDate = eventDate;
        EventProgress = eventProgress;
        EventType = eventType;
        EventSpecificColor = eventSpecificColor;
        ParentClassCalendarID = parentClassCalendarID;
        ParentClassSectionID = parentClassSectionID;
        ImportSourceID = importSourceID;
    }

    public Assignment() {}

    public int getAssignmentID() {
        return AssignmentID;
    }

    public String getEventName() {
        return EventName;
    }

    public Date getEventDate() {
        return EventDate;
    }

    public int getEventProgress() {
        return EventProgress;
    }

    public String getEventType() {
        return EventType;
    }

    public int getEventSpecificColor() {
        return EventSpecificColor;
    }

    public int getParentClassCalendarID() {
        return ParentClassCalendarID;
    }

    public int getParentClassSectionID() {
        return ParentClassSectionID;
    }

    public int getImportSourceID() {
        return ImportSourceID;
    }

    @Override
    public String toString() {
        return String.format("Assignment(Name=%s, Date=%s)", this.EventName, this.EventDate);

    }
}
