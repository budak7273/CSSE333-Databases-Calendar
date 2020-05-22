import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
		HashMap<String, Integer> mapping = getFriendlyStringIDMapping(getListAllCalendarsOverall(false));
        String[] choices = {};
        String calendarIDString = null;
		try {
			calendarIDString = JOptionPane.showInputDialog(null, "Select Calendar to Join\n(All calendars are listed)", "Calendar Join Selection", JOptionPane.QUESTION_MESSAGE, null, mapping.keySet().toArray(choices), 1).toString();
		} catch (Exception ignored) {
			//do nothing, we don't care
		}
		if (calendarIDString == null) return;
		int calendarID = mapping.get(calendarIDString);
		
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
		HashMap<String, Integer> mapping = getFriendlyStringIDMapping(listAllFollowedCalendars(false));
        String[] choices = {};
        String calendarIDString = null;
		try {
			calendarIDString = JOptionPane.showInputDialog(null, "Select Calendar to Leave\n(Followed calendars are listed)", "Calendar Leave Selection", JOptionPane.QUESTION_MESSAGE, null, mapping.keySet().toArray(choices), 1).toString();
		} catch (Exception ignored) {
			//do nothing, we don't care
		}
        if (calendarIDString == null) return;
		int calendarID = mapping.get(calendarIDString);

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
	
	public ArrayList<CalendarListing> getListAllCalendarsOverall(boolean display) {
		connect();
		
		String paramQueryString = "{call get_ClassCalendar_List}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			StringBuilder sb = new StringBuilder();
			sb.append("CalendarID   Name   Creator\n");
			ArrayList<CalendarListing> calendars = new ArrayList<CalendarListing>();
			
			ResultSet rs = paramQueryPS.executeQuery();
            while (rs.next()) {
            	calendars.add(new CalendarListing(rs.getInt("CalendarID"), rs.getString("Name"), rs.getString("Creator")));
            }
            
            System.out.printf("Database Returned %d ClassCalendars.\n", calendars.size());
            if(display) {
            	for (CalendarListing calendarListing : calendars) {
					sb.append(calendarListing.toString());
					sb.append("\n");
				}
            	JOptionPane.showMessageDialog(null, sb.toString(), "Available ClassCalendars", JOptionPane.INFORMATION_MESSAGE);
            }
            return calendars;
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
		return new ArrayList<CalendarListing>();
	}
	
	public class CalendarListing {
		public int CalendarID;
		public String Name;
		public String Creator;
		
		public CalendarListing(int calID, String name, String creator) {
			CalendarID = calID;
			Name = name;
			Creator = creator;
		}
		
		public String toString() {
			return "" + CalendarID + "  " + Name + "  " + Creator;
		}
		
	}
	
	public ArrayList<CalendarListing> getAllAccessibleCalendars() {
		connect();
		
		String paramQueryString = "{call get_user_ClassCalendar_List(?)}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			StringBuilder sb = new StringBuilder();
			sb.append("CalendarID   Name   Creator\n");
			paramQueryPS.setString(1, username);
			ArrayList<CalendarListing> calendars = new ArrayList<CalendarListing>();
			
			ResultSet rs = paramQueryPS.executeQuery();
            while (rs.next()) {
            	calendars.add(new CalendarListing(rs.getInt("CalendarID"), rs.getString("Name"), rs.getString("Creator")));
            }
            
            System.out.printf("Database Returned %d Accessible ClassCalendars.\n", calendars.size());
            /*for (CalendarListing calendarListing : calendars) {
				sb.append(calendarListing);
				sb.append("\n");
			}
            return sb.toString();*/
            return calendars;
		} catch (SQLException e) {
			System.out.println("Listing FAILED");
			JOptionPane.showMessageDialog(null, "An error occurred in obtaining the accessible calendar list. See the printed stack trace.");
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
		return new ArrayList<CalendarListing>();
	}
	
	public HashMap<String, Integer> getFriendlyStringIDMapping(ArrayList<CalendarListing> calendars) {
        HashMap<String, Integer> mapping = new  HashMap<String, Integer>();
        for (CalendarSharingHandler.CalendarListing calendarListing : calendars) {
			mapping.put(calendarListing.toString(), calendarListing.CalendarID);
		}
        return mapping;
	}
	
	public ArrayList<CalendarListing> listAllFollowedCalendars(boolean display) {
		connect();
		
		String paramQueryString = "{call get_All_Followed_Calendars_Of_User(?)}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			
			paramQueryPS.setString(1, username);
			
			StringBuilder sb = new StringBuilder();
			sb.append("CalendarID   Name   Creator\n");
			ArrayList<CalendarListing> calendars = new ArrayList<CalendarListing>();
			
			ResultSet rs = paramQueryPS.executeQuery();
            while (rs.next()) {
            	calendars.add(new CalendarListing(rs.getInt("CalendarID"), rs.getString("Name"), rs.getString("Creator")));
            }
            
            System.out.printf("Database Returned %d Followed ClassCalendars.\n", calendars.size());
            if(display) {
            	for (CalendarListing calendarListing : calendars) {
					sb.append(calendarListing.toString());
					sb.append("\n");
				}
            	JOptionPane.showMessageDialog(null, sb.toString(), "Followed ClassCalendars", JOptionPane.INFORMATION_MESSAGE);
            }
	        return calendars;
		} catch (SQLException e) {
			System.out.println("Listing FAILED");
			JOptionPane.showMessageDialog(null, "An error occurred in obtaining the followed calendar list. See the printed stack trace.");
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
		return new ArrayList<CalendarListing>();
	}
}
