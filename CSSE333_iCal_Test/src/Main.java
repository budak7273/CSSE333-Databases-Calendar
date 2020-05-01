import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.util.Configurator;
import us.k5n.ical.ICalendarParser;


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
		System.out.println(rVal);
				
		File iCalFile = fc.getSelectedFile();
		System.out.println("You selected the file " + iCalFile.getName());
		
		//k5n(iCalFile);
		//ical4j(iCalFile);
		biweekly(iCalFile);
		
		
	}
	
	private static void k5n(File calfile) {
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
	}
	
	private static void ical4j(File calfile) {
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

		
	}

	private static void biweekly(File calfile) {
		
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

		List<VEvent> allEvents = ical.getEvents();
		System.out.println("This iCal file contains " + allEvents.size() + " events.");
		for (VEvent thisEvent : allEvents) {
			System.out.print('\t');
			System.out.println(thisEvent.getSummary().getValue());
		}
		
//		VEvent event = ical.getEvents().get(0);
//		String summary = event.getSummary().getValue();
//		System.out.println(summary);
	}
}
