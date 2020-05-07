import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class AssignmentService {
    private DatabaseConnectionService dbService = null;

    public AssignmentService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ArrayList<Assignment> getAllAssignmentsForUser(String username, String password) {
        ArrayList<Assignment> assignments = new ArrayList<>();
        System.out.println("connect returned " + dbService.connect("appUserCalendarDB", "Password123"));
        Connection con = dbService.getConnection();

        Statement state = null;
        try {
            state = con.createStatement();
            String query = "SELECT AssignmentID, EventName, EventDate, EventProgress, Type, EventSpecificColor, ParentClassCalendarID, ParentClassSectionID, ImportSourceID, CalendarColor, ClassTime, ClassName, ParentUserID AS UserID\n" +
                    "FROM Assignment A\n" +
                    "INNER JOIN ClassCalendar CC\n" +
                    "ON A.ParentClassCalendarID = CC.ClassCalendarID\n" +
                    "WHERE ParentUserID = '" + username + "'"; //TODO: SQL Injection VULNERABLE
            ResultSet resultSet = state.executeQuery(query);
            while (resultSet.next()) {
                int AssignmentID = resultSet.getInt("AssignmentID");
                String EventName = resultSet.getString("EventName");
                Date EventDate = resultSet.getDate("EventDate");
                int EventProgress = resultSet.getInt("EventProgress");
                String Type = resultSet.getString("Type");
                int EventSpecificColor = resultSet.getInt("EventSpecificColor");
                int ParentClassCalendarID = resultSet.getInt("ParentClassCalendarID");
                int ParentClassSectionID = resultSet.getInt("ParentClassSectionID");
                int ImportSourceID = resultSet.getInt("ImportSourceID");

                Assignment thisAssignment = new Assignment(AssignmentID, EventName, EventDate, EventProgress, Type, EventSpecificColor, ParentClassCalendarID, ParentClassSectionID, ImportSourceID);
                assignments.add(thisAssignment);
            }
            //TODO Add Data to Database, Remove following lines:
//            Assignment dummyAssignment = new Assignment(1,"Dummy Event", new Date(2020, 4,28), 20, "Exam", 0x66FF69, 22, 323, 45);
//            assignments.add(dummyAssignment);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (state != null)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        dbService.closeConnection();
        return assignments;
    }

}
