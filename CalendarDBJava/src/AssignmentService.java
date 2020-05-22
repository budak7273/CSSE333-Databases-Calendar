
import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AssignmentService {
    private DatabaseConnectionService dbService;
    private CalendarSharingHandler calendarSharingHandler;
    private String username;

    public AssignmentService(DatabaseConnectionService dbService, String username, CalendarSharingHandler calShare) {
        this.dbService = dbService;
        this.username = username;
        this.calendarSharingHandler = calShare;
    }

    public ArrayList<Assignment> getAllAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<>();

        System.out.printf("Fetching assignments for User %s", username);
        dbService.connect();
        Connection con = dbService.getConnection();
        PreparedStatement getAllAssignmentsPS = null;

        try {
            String getAllAssignmentsQueryString = "{call get_All_Assignments_Of_User(?)}";
            getAllAssignmentsPS = this.dbService.getConnection().prepareStatement(getAllAssignmentsQueryString);
            getAllAssignmentsPS.setString(1, username);

            ResultSet rs = getAllAssignmentsPS.executeQuery();
            while (rs.next()) {
                int AssignmentID = rs.getInt("AssignmentID");
                String EventName = rs.getString("EventName");
                Date EventDate = rs.getDate("EventDate");
                int EventProgress = rs.getInt("EventProgress");
                String Type = rs.getString("Type");
                int EventSpecificColor = rs.getInt("EventSpecificColor");
                int ParentClassCalendarID = rs.getInt("ParentClassCalendarID");
                int ParentClassSectionID = rs.getInt("ParentClassSectionID");
                int ImportSourceID = rs.getInt("ImportSourceID");
                String EventDescription = rs.getString("EventDescription");

                Assignment thisAssignment = new Assignment(AssignmentID, EventName, EventDate, EventProgress, Type, EventSpecificColor, ParentClassCalendarID, ParentClassSectionID, ImportSourceID, EventDescription);
                assignments.add(thisAssignment);
            }
            System.out.printf("\tDatabase Returned %d Assignments for user %s.\n", assignments.size(), username);

//            Assignment dummyAssignment = new Assignment(1,"Dummy Event", new Date(2020, 4,28), 20, "Exam", 0x66FF69, 22, 323, 45);
//            assignments.add(dummyAssignment);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getAllAssignmentsPS != null)
                    System.out.println("Closing getAllAssignmentsForUser Connection");
                    getAllAssignmentsPS.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return assignments;
    }

    /**
     * prompts the user to create a new assignment, then adds it to the database.
     * Does not update assignment list or redraw calendar, so maybe do that after calling this?
     * @return true if assignment was succesfully added, false otherwise.
     */
    public boolean createNewAssignmentPrompt() {
        String eventName;
        Date eventDate;
        int eventProgress;
        String eventType;
        int eventSpecificColor;
        int parentClassCalendarID;
        String eventDescription;

        String eventDateString = JOptionPane.showInputDialog("Enter Event Date in MM/DD/YYYY format");
        if (eventDateString == null) return false;
        try {
            eventDate = new Date(eventDateString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Date not in specified format.\nPlease try again.");
            return false;
        }

        eventName = JOptionPane.showInputDialog("Enter Assignment Name: ");
        if (eventName == null) return false;

        String eventProgressString = JOptionPane.showInputDialog("Enter Event Progress\n(0-100, inclusive): ");
        if (eventProgressString == null) return false;
        try {
            eventProgress = Integer.parseInt(eventProgressString);
            if (eventProgress > 100 || eventProgress < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Event Progress not in specified format.\nPlease try again.");
            return false;
        }

        eventType = JOptionPane.showInputDialog("Enter Assignment Type (Test, Quiz, etc.): ");
        if (eventType == null) return false;

        eventSpecificColor = JColorChooser.showDialog(null, "Choose an Event Color", null).getRGB();
        System.out.println(eventSpecificColor);
        
        HashMap<String, Integer> mapping = calendarSharingHandler.getFriendlyStringIDMapping(calendarSharingHandler.getAllAccessibleCalendars());
        String[] choices = {};
        String parentClassCalendarIDString = JOptionPane.showInputDialog(null, "Select an existing Calendar to add this to.", "Calendar Selection", JOptionPane.QUESTION_MESSAGE, null, mapping.keySet().toArray(choices), 1).toString();
        if (parentClassCalendarIDString == null) return false;
        
        
        try {
            parentClassCalendarID = mapping.get(parentClassCalendarIDString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Class Calendar is an int.\nPlease try again.");
            return false;
        }

        eventDescription = JOptionPane.showInputDialog("Enter Assignment Description");
        if (eventDescription == null) return false;

        if (insertAssignmentToDB(eventName, eventDate, eventProgress, eventType, eventSpecificColor, parentClassCalendarID, eventDescription)) { // add succeeded
            JOptionPane.showMessageDialog(null, "Success! Your Assignment was successfully added.");
            return true;
        } else {    // add failed
            JOptionPane.showMessageDialog(null, "Assignment Insert Failed. Please try again later.");
            return false;
        }
    }

    private boolean insertAssignmentToDB(String eventName, Date eventDate, int eventProgress, String eventType, int eventSpecificColor, int parentClassCalendarID, String eventDescription) {
        System.out.printf("Inserting new assignment with name\"%s\", date \"%s\", progress \"%d\", type \"%s\", color \"%d\", parentClassCalendarID \"%d\", and description \"%s\"...", eventName, eventDate, eventProgress, eventType, eventSpecificColor, parentClassCalendarID, eventDescription);

        dbService.connect();
        Connection con = dbService.getConnection();
        CallableStatement insertAssignmentsPS = null;
        int returnedStatus = 5001;

        try {
            String insertAsQueryString = "{? = call insert_Assignment(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            insertAssignmentsPS = this.dbService.getConnection().prepareCall(insertAsQueryString);
            int i = 1;
            insertAssignmentsPS.registerOutParameter(i++, Types.INTEGER);
            insertAssignmentsPS.setString(i++, eventName);
            insertAssignmentsPS.setDate(i++, new java.sql.Date(eventDate.getTime()));
            insertAssignmentsPS.setInt(i++, eventProgress);
            insertAssignmentsPS.setString(i++, eventType);
            insertAssignmentsPS.setInt(i++, eventSpecificColor);
            insertAssignmentsPS.setInt(i++, parentClassCalendarID);
            insertAssignmentsPS.setInt(i++, -1); // -1 indicates no (null) class section id (class section is only for downstream duplication)
            insertAssignmentsPS.setString(i++, null);
            insertAssignmentsPS.setString(i++, eventDescription);

            insertAssignmentsPS.execute();

            returnedStatus = insertAssignmentsPS.getInt(1);
            System.out.println("insert_Assignment returned " + returnedStatus);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (insertAssignmentsPS != null) {
                    System.out.println("Closing insertAssignmentToDB Connection");
                    insertAssignmentsPS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnedStatus == 0;
    }

    public ArrayList<Assignment> getAllAssignmentsSortedByDate() {
        ArrayList<Assignment> assignmentList = getAllAssignments();
        getAllAssignments().sort(new Assignment.ComparatorByDate());
        return assignmentList;
    }

}
