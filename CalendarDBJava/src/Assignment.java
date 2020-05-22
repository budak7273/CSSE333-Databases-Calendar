import java.util.Comparator;
import java.util.Date;

import biweekly.component.VEvent;

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
    private String EventDescription;

    public Assignment(int assignmentID, String eventName, Date eventDate, int eventProgress, String eventType, int eventSpecificColor, int parentClassCalendarID, int parentClassSectionID, int importSourceID, String eventDescription) {
        AssignmentID = assignmentID;
        EventName = eventName;
        EventDate = eventDate;
        EventProgress = eventProgress;
        EventType = eventType;
        EventSpecificColor = eventSpecificColor;
        ParentClassCalendarID = parentClassCalendarID;
        ParentClassSectionID = parentClassSectionID;
        ImportSourceID = importSourceID;
        EventDescription = eventDescription;
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
    
    public VEvent toBiweeklyEvent() {
    	VEvent event = new VEvent();
    	event.setSummary(EventName);
    	//System.out.println("date of " + EventName + " is " + EventDate.getTime() );
    	event.setDateStart(EventDate);
    	event.setColor(new biweekly.property.Color("chartreuse")); //iCal only supports X11 safe colors, and mapping rgb to that is a huge pain and not worth the time
    	event.setDescription(EventDescription + "\nProgess: " + getEventProgress() + "\rType: " + EventType);
    	return event;
    }

    @Override
    public String toString() {
        return String.format("Assignment(Name=%s, Date=%s)", this.EventName, this.EventDate);

    }

    public String getEventDescription() {
        return EventDescription;
    }


    public static class ComparatorByDate implements Comparator<Assignment> {
        @Override
        public int compare(Assignment o1, Assignment o2) {
            return o1.getEventDate().compareTo(o2.EventDate);
        }
    }
}
