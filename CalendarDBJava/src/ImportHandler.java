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

	public int RUN_COUNTER = 0;
	private DatabaseConnectionService dbService;
	
	public ImportHandler(DatabaseConnectionService dbS) {
		dbService = dbS;
	}
	
	public boolean promptICalImport() {
		RUN_COUNTER++;
		
		if(RUN_COUNTER >= 2) { //only run on the second call and above - UGLY fix for current method of prompting (second paint) TODO replace
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
			fc.setFileFilter(new FileNameExtensionFilter("iCalendar Files (.ics)", "ics"));
			
			System.out.println("Select an iCal file to test.");
			int rVal = fc.showOpenDialog(null);
			
			if(rVal == 0) { //successfully opened file
				File iCalFile = fc.getSelectedFile();
				System.out.println("You selected the file " + iCalFile.getName());
				try {
					System.out.println("Parsing with Biweekly...");
					return biweekly(iCalFile);
				} catch (Exception e) {
					System.out.println("Biweekly failed to parse the file");
					return false;
				}
				
			}
			
			/*VEvent tmp = new VEvent();
			tmp.setSummary("DemoJavaVEvent");
			return addAssignmentFromICalParse(tmp, 0, 0);*/
		}
		System.out.println("called but ignored");
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
        String paramQueryString = "{call insert_Assignment(?,?,?,?,?,?,?,?)}";
        
        PreparedStatement paramQueryPS = null;
		try {
			paramQueryPS = this.dbService.getConnection().prepareStatement(paramQueryString);
			
			paramQueryPS.setString(1, event.getSummary().getValue());//"DemoJavaEvent");
			Calendar cal = Calendar.getInstance();
			paramQueryPS.setDate(2, new Date(cal.getTimeInMillis()), cal); //TODO look into the int, Date, Calendar constructor for timezone adjustment?
			paramQueryPS.setByte(3, (byte) 0);
			paramQueryPS.setString(4, null);
			paramQueryPS.setInt(5, 0);
			paramQueryPS.setInt(6, 2);//TODO set this to the passed parentClassCalendarID instead of testing ID#2
			paramQueryPS.setInt(7, 3); //TODO set this to the passed parentClassSectionID instead of testing ID#3
			paramQueryPS.setInt(8, 1); //TODO set this to the passed importSourceID instead of testing ID#1
			
	        int rCode = paramQueryPS.executeUpdate(); //for some reason this returns 1 for success and -1 for failure. This is not documented seemingly anywhere I could find?
	        //System.out.println(rCode);
	        if(rCode >= 0) { //for some reason it still returns -1 even on a successful add?? Task failed successfully!
//	        	return true;
	        } else {
//	        	JOptionPane.showMessageDialog(null, "An error occurred when processing the Event");
//	        	return false;
	        }
	        return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "An error occurred in adding the restaurant. See the printed stack trace.");
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
        return false;
	}
	
	private boolean biweekly(File calfile) throws Exception {
		
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
		
		//System.out.println("READ:\n\r" + str);
		
		ICalendar ical = Biweekly.parse(str).first();
		
		if(ical == null) {
			System.out.println("The calendar file you entered could not be parsed");
			return false;
		} else {
			List<VEvent> allEvents = ical.getEvents();
			System.out.println("This iCal file contains " + allEvents.size() + " events.");
			for (VEvent thisEvent : allEvents) {
				try {
					printEventDetails(thisEvent, false);
					System.out.println("attempting to upload...");
					addAssignmentFromICalParse(thisEvent, 0, 0); //TODO get and use parent cal id and import source ID
				} catch (NullPointerException ex) {
					System.out.println("A property of this event failed to be displayed.");
				}
			}
		}
		return true;
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
