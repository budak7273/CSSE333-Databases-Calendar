
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class AssignmentService {
    private DatabaseConnectionService dbService;

    public AssignmentService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ArrayList<Assignment> getAllAssignmentsForUser(String username) {
        ArrayList<Assignment> assignments = new ArrayList<>();

        dbService.connect();
        Connection con = dbService.getConnection();
        Statement statement = null;

        try {
            statement = con.createStatement();
            String query = "SELECT AssignmentID, EventName, EventDate, EventProgress, Type, EventSpecificColor, ParentClassCalendarID, ParentClassSectionID, ImportSourceID, CalendarColor, ClassTime, ClassName, ParentUserID AS UserID\n" +
                    "FROM Assignment A\n" +
                    "INNER JOIN ClassCalendar CC\n" +
                    "ON A.ParentClassCalendarID = CC.ClassCalendarID\n" +
                    "WHERE ParentUserID = '" + username + "'"; //TODO: SQL Injection VULNERABLE
            ResultSet rs = statement.executeQuery(query);
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

                Assignment thisAssignment = new Assignment(AssignmentID, EventName, EventDate, EventProgress, Type, EventSpecificColor, ParentClassCalendarID, ParentClassSectionID, ImportSourceID);
                assignments.add(thisAssignment);
            }
            System.out.printf("Database Returned %d Assignments for user %s.\n", assignments.size(), username);

//            Assignment dummyAssignment = new Assignment(1,"Dummy Event", new Date(2020, 4,28), 20, "Exam", 0x66FF69, 22, 323, 45);
//            assignments.add(dummyAssignment);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return assignments;
    }

}
