import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
		
		k5n(iCalFile);
		
		
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
}
