import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JFileChooser;
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
//import net.fortuna.ical4j.data.CalendarBuilder;
//import net.fortuna.ical4j.model.Calendar;
//import net.fortuna.ical4j.model.Component;
//import net.fortuna.ical4j.model.Property;
//import net.fortuna.ical4j.model.component.CalendarComponent;
//import net.fortuna.ical4j.util.Configurator;
//import us.k5n.ical.ICalendarParser;


/**
 * Class that tests an iCal Parser framework. Prompts the user to open an iCal file, then operates on the file.
 * @author budakrc
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setFileFilter(new FileNameExtensionFilter("iCalendar Files (.ics)", "ics"));
		
		System.out.println("Select an iCal file to test.");
		int rVal = fc.showOpenDialog(null);
//		System.out.println(rVal);
				
		if(rVal == 0) { //successfully opened file
			File iCalFile = fc.getSelectedFile();
			System.out.println("You selected the file " + iCalFile.getName());
			
//			try { 	
//				System.out.println("Parsing with k5n...");
//				k5n(iCalFile);
//			} catch (Exception e) {
//				System.out.println("k5n failed to parse the file");
//			}
//			
//			try {
//				System.out.println("Parsing with ical4j...");
//				ical4j(iCalFile);
//			} catch (Exception e) {
//				System.out.println("ical4j failed to parse the file");
//			}
			
			try {
				System.out.println("Parsing with Biweekly...");
				biweekly(iCalFile);
			} catch (Exception e) {
				System.out.println("Biweekly failed to parse the file");
			}
			
		}
		
		
	}
	
	/**
	 * Tries to use k5n-ical to process the calendar file
	 * @param calfile
	 */
	/*private static void k5n(File calfile) {
		ICalendarParser icp = new ICalendarParser(0);
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(calfile));
			icp.parse(br);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		System.out.println(icp.toString());
	}*/
	
	/**
	 * Tries to use ical4j to process the calendar file
	 * @param calfile
	 */
	/*private static void ical4j(File calfile) {
		//http://ical4j.sourceforge.net/introduction.html
		
		FileInputStream fin;
		try {
			fin = new FileInputStream(calfile);
			
			CalendarBuilder builder = new CalendarBuilder();

			Calendar calendar = builder.build(fin);
			
			for (Iterator<CalendarComponent> i = calendar.getComponents().iterator(); i.hasNext();) {
			    Component component = (Component) i.next();
			    System.out.println("Component [" + component.getName() + "]");

			    for (Iterator<Property> j = component.getProperties().iterator(); j.hasNext();) {
			        Property property = (Property) j.next();
			        System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}*/

	/**
	 * Tries to use Biweekly to process the calendar file
	 * @param calfile
	 * @throws Exception when the file can't be parsed
	 */
	private static void biweekly(File calfile) throws Exception {
		
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
		}
		
		//System.out.println("READ:\n\r" + str);
		
		ICalendar ical = Biweekly.parse(str).first();
		
		if(ical == null)
			throw new Exception("The calendar file you entered could not be parsed");
		else {
			List<VEvent> allEvents = ical.getEvents();
			System.out.println("This iCal file contains " + allEvents.size() + " events.");
			for (VEvent thisEvent : allEvents) {
				try {
					printEventDetails(thisEvent, false);
				} catch (NullPointerException ex) {
					System.out.println("A property of this event failed to be displayed.");
				}
			}
		}
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
