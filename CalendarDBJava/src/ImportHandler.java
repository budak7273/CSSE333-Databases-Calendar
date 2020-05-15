import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Color;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.Description;
import biweekly.property.ICalProperty;
import biweekly.property.RecurrenceRule;
import biweekly.util.ListMultimap;

public class ImportHandler {

	private DatabaseConnectionService dbService;
	
	String serverNameToUse;
	String databaseNameToUse;
	String username;
	
	public ImportHandler(String serverName, String databaseName, String usernameForImport) {
		serverNameToUse = serverName;
		databaseNameToUse = databaseName;
		username = usernameForImport;
	}
	
	public boolean promptICalImport() {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setFileFilter(new FileNameExtensionFilter("iCalendar Files (.ics)", "ics"));
		
		System.out.println("Select an iCal file to test.");
		int rVal = fc.showOpenDialog(null);
		
		if(rVal == 0) { //successfully opened file
			File iCalFile = fc.getSelectedFile();
			System.out.println("You selected the file " + iCalFile.getName());
			try {
				System.out.println("Parsing with Biweekly...");
				dbService = new DatabaseConnectionService(serverNameToUse, databaseNameToUse);
				dbService.connect();
				boolean tmp = biweeklyParse(iCalFile);
				dbService.closeConnection();
				return tmp;
			} catch (Exception e) {
				System.out.println("Biweekly failed to parse the file");
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Handles a VEvent from an ical file and adds it to the database
	 * @param event
	 * @param parentClassCalendarID
	 * @param importSourceID The import source obtained from running the create import source sproc
	 * @return
	 */
	public boolean addAssignmentFromICalParse(VEvent event, int parentClassCalendarID, int importSourceID) {
        String paramQueryString = "{call insert_Assignment(?,?,?,?,?,?,?,?,?)}";
        System.out.println("Using calID " + parentClassCalendarID + " isID " + importSourceID);
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			
			paramQueryPS.setString(1, event.getSummary().getValue());//"DemoJavaEvent");
			Calendar cal = Calendar.getInstance();
			//paramQueryPS.setDate(2, new Date(cal.getTimeInMillis()), cal); //TODO look into the int, Date, Calendar constructor for timezone adjustment?
			long millis = event.getDateStart().getValue().getTime();
			paramQueryPS.setDate(2, new Date(millis), cal); //TODO look into the int, Date, Calendar constructor for timezone adjustment?
			paramQueryPS.setByte(3, (byte) 0);
			paramQueryPS.setString(4, null);
			paramQueryPS.setInt(5, 0);
			paramQueryPS.setInt(6, parentClassCalendarID);//for testing, use ID#2
			paramQueryPS.setInt(7, 4); //TODO set this to the passed parentClassSectionID instead of testing ID#4
			paramQueryPS.setInt(8, importSourceID); //for testing, use ID#1
			paramQueryPS.setString(9, event.getDescription().getValue());
			
	        int rCode = paramQueryPS.executeUpdate(); //for some reason this returns 1 for success and -1 for failure. This is not documented seemingly anywhere I could find?
	        //System.out.println(rCode);
	        if(rCode >= 0) { //for some reason it still returns -1 even on a successful add?? Task failed successfully!
//	        	return true;
	        } else {
//	        	JOptionPane.showMessageDialog(null, "An error occurred when processing the Event");
//	        	return false;
	        }
	        System.out.println("Upload of assignment " + event.getSummary().getValue() + " succeeded");
	        return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "An error occurred in adding the assignment. See the printed stack trace.");
			e.printStackTrace();
		} finally {
			try {
				if (paramQueryPS != null) 
					paramQueryPS.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "An error occurred in closing the statement. See the printed stack trace.");
				e.printStackTrace();
			}
		}
		System.out.println("Upload of assignment " + event.getSummary().getValue() + " FAILED");
        return false;
	}
	
	private boolean biweeklyParse(File calfile) throws Exception {
		
		StringBuilder strBuild = new StringBuilder();
		String str= "";
		try (BufferedReader br = new BufferedReader(new FileReader(calfile));){
			
			String line;
			while ((line = br.readLine()) != null) {
			    if (line.isEmpty()) {
			        break;
			    } else {
			    	strBuild.append(line);
			    	strBuild.append("\r\n");
			    }
			}
			str = strBuild.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		ICalendar ical = Biweekly.parse(str).first();
		
		if(ical == null) {
			System.out.println("The calendar file you entered could not be parsed");
			return false;
		} else {
			List<VEvent> allEvents = ical.getEvents();
			System.out.println("This iCal file contains " + allEvents.size() + " events.");
			
			Token newIDsToUse = prepareForImport();
			if(newIDsToUse == null)
				return false;
			
			for (VEvent thisEvent : allEvents) {
				try {
					printEventDetails(thisEvent, false);
					System.out.println("attempting to upload...");
					addAssignmentFromICalParse(thisEvent, newIDsToUse.classCalendarID, newIDsToUse.importSourceID);
				} catch (NullPointerException ex) {
					System.out.println("A property of this event failed to be processed.");
				}
			}
		}
		return true;
	}
	
	private class Token {
		public int importSourceID;
		public int classCalendarID; 
		
		public Token(int ccID, int isID) {
			importSourceID = isID;
			classCalendarID = ccID;
		}
	}
	
	private Token prepareForImport() {
		String paramQueryString = "{call prepareImport(?,?)}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			
			paramQueryPS.setString(1, username);
			paramQueryPS.setString(2, JOptionPane.showInputDialog("Enter a name to use for the imported calendar"));
			
			int calID = -1;
			int isID = -1;
			
			ResultSet rs = paramQueryPS.executeQuery();
            while (rs.next()) {
                calID = rs.getInt("NewCalendarID");
                isID = rs.getInt("NewImportSourceID");
            }
            System.out.println("New calendar " + calID + " and import souce " + isID + " created for import process");
            return new Token(calID, isID);
            
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "An error occurred in preparing for the import. See the printed stack trace.");
			e.printStackTrace();
		} finally {
			try {
				if (paramQueryPS != null) 
					paramQueryPS.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "An error occurred in closing the statement. See the printed stack trace.");
				e.printStackTrace();
			}
		}
		System.out.println("Preparing for import FAILED");
        return null;
	}
	
	/**
	 * @param VEvent event - event to print the details of
	 * @param boolean dumpAll - if the method should print _every_ property of the event or just the nicely formatted ones
	 * @throws NullPointerException when a parameter could not be parsed from this method and Rob was a dummy and forgot to write a check for it
	 */
	private static void printEventDetails(VEvent event, boolean dumpAll) throws NullPointerException {
		if(!dumpAll) {
			System.out.println('\n');
			System.out.println(event.getSummary().getValue());
			Color clr = event.getColor();
			System.out.println("Color: " + ((clr == null) ? "<no color>" : clr.getValue()));
			Description dr = event.getDescription();
			System.out.println("Description: " + ((dr == null || dr.getValue().trim().isEmpty()) ? "<no description>" : dr.getValue()));
			DateStart sd = event.getDateStart();
			System.out.println("Start: " + ((sd == null) ? "<no start date?>" : sd.getValue()));
			DateEnd ed = event.getDateEnd();
			System.out.println("End: " + ((ed == null) ? "<no end date -> recurring event>" : ed.getValue()));
			RecurrenceRule rr = event.getRecurrenceRule();
			System.out.println("Recurrence: " + ((rr == null) ? "<no recurrence>" : rr.getValue().toString()));
		} else {
			ListMultimap<Class<? extends ICalProperty>, ICalProperty> mapping = event.getProperties();
			Iterator<Entry<Class<? extends ICalProperty>, List<ICalProperty>>> mapIterator = mapping.iterator();
			while (mapIterator.hasNext()) {
				Entry<Class<? extends ICalProperty>, List<ICalProperty>> thisItem = mapIterator.next();
				System.out.print('[' + thisItem.getKey().getSimpleName() + "] : ");
				for (ICalProperty thisPropertyEntry : thisItem.getValue()) {
					if (thisPropertyEntry == null) {
						System.out.println("<no data?>");
					} else {
						System.out.println(thisPropertyEntry.toString());
					}
				}
			}
		}
		
	}
}
