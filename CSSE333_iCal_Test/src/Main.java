import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


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
	}
}
