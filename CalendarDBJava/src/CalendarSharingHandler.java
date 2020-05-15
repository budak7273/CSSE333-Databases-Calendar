import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class CalendarSharingHandler {

	private DatabaseConnectionService dbService;
	
	private String serverNameToUse;
	private String databaseNameToUse;
	
	private String username;
	
	public CalendarSharingHandler(String serverName, String databaseName, String usernameOfUser) {
		serverNameToUse = serverName;
		databaseNameToUse = databaseName;
		username = usernameOfUser;
	}
	
	private void connect() {
		dbService = new DatabaseConnectionService(serverNameToUse, databaseNameToUse);
		dbService.connect();
	}
	
	public void followCalendar() {
		int calendarID = Integer.parseInt(JOptionPane.showInputDialog("Enter the CalendarID to follow"));
		
		connect();
		
		String paramQueryString = "{call insert_UserCalendarSharing_Relationship(?,?)}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			
			paramQueryPS.setString(1, username);
			paramQueryPS.setInt(2, calendarID);
			
	        int rCode = paramQueryPS.executeUpdate(); //for some reason this returns 1 for success and -1 for failure. This is not documented seemingly anywhere I could find?
	        //System.out.println(rCode);
	        if(rCode >= 0) { //for some reason it still returns -1 even on a successful add?? Task failed successfully!
//	        	return true;
	        } else {
//	        	JOptionPane.showMessageDialog(null, "An error occurred when processing the Event");
//	        	return false;
	        }
	        JOptionPane.showMessageDialog(null, "The calendar was successfully followed.");
		} catch (SQLException e) {
			System.out.println("Following of calendar " + calendarID + " FAILED");
			JOptionPane.showMessageDialog(null, "An error occurred in following the calendar. Perhaps you were already following it? See the printed stack trace.");
			e.printStackTrace();
		} finally {
			try {
				if (paramQueryPS != null) 
					paramQueryPS.close();
			} catch (SQLException e) {
				System.out.println("Following of calendar " + calendarID + " FAILED");
				JOptionPane.showMessageDialog(null, "An error occurred in closing the statement. See the printed stack trace.");
				e.printStackTrace();
			}
		}

		dbService.closeConnection();
	}
	
	public void unfollowCalendar() {
		int calendarID = Integer.parseInt(JOptionPane.showInputDialog("Enter the CalendarID to unfollow"));
		connect();
		
		String paramQueryString = "{call delete_UserCalendarSharing_Relationship(?,?)}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			
			paramQueryPS.setString(1, username);
			paramQueryPS.setInt(2, calendarID);
			
	        int rCode = paramQueryPS.executeUpdate(); //for some reason this returns 1 for success and -1 for failure. This is not documented seemingly anywhere I could find?
	        //System.out.println(rCode);
	        if(rCode >= 0) { //for some reason it still returns -1 even on a successful add?? Task failed successfully!
//	        	return true;
	        } else {
//	        	JOptionPane.showMessageDialog(null, "An error occurred when processing the Event");
//	        	return false;
	        }
	        JOptionPane.showMessageDialog(null, "The calendar was successfully unfollowed, assuming you were following it.");
		} catch (SQLException e) {
			System.out.println("Unfollowing of calendar " + calendarID + " FAILED");
			JOptionPane.showMessageDialog(null, "An error occurred in unfollowing the calendar. See the printed stack trace.");
			e.printStackTrace();
		} finally {
			try {
				if (paramQueryPS != null) 
					paramQueryPS.close();
			} catch (SQLException e) {
				System.out.println("Unfollowing of calendar " + calendarID + " FAILED");
				JOptionPane.showMessageDialog(null, "An error occurred in closing the statement. See the printed stack trace.");
				e.printStackTrace();
			}
		}
		
		dbService.closeConnection();
	}
	
	public void listAllCalendars() {
		connect();
		
		String paramQueryString = "{call get_ClassCalendar_List}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			StringBuilder sb = new StringBuilder();
			sb.append("CalendarID   Name   Creator\n");
			int counter = 0;
			
			ResultSet rs = paramQueryPS.executeQuery();
            while (rs.next()) {
            	sb.append(rs.getInt("CalendarID"));
            	sb.append("   ");
            	sb.append(rs.getString("Name"));
            	sb.append("   ");
            	sb.append(rs.getString("Creator"));
            	sb.append('\n');
            	counter++;
            }
            
            System.out.printf("Database Returned %d ClassCalendars.\n", counter);
	        JOptionPane.showMessageDialog(null, sb.toString());
		} catch (SQLException e) {
			System.out.println("Listing FAILED");
			JOptionPane.showMessageDialog(null, "An error occurred in obtaining the calendar list. See the printed stack trace.");
			e.printStackTrace();
		} finally {
			try {
				if (paramQueryPS != null) 
					paramQueryPS.close();
			} catch (SQLException e) {
				System.out.println("Listing FAILED");
				JOptionPane.showMessageDialog(null, "An error occurred in closing the statement. See the printed stack trace.");
				e.printStackTrace();
			}
		}
		
		dbService.closeConnection();
	}
}
