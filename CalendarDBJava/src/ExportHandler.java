import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import biweekly.Biweekly;
import biweekly.ICalendar;

public class ExportHandler {

	public static void convertAssignmentsToICal(ArrayList<Assignment> assignmentList) {
		ICalendar cal = new ICalendar();
		for (Assignment assignment : assignmentList) {
			cal.addEvent(assignment.toBiweeklyEvent());
		}
		
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setFileFilter(new FileNameExtensionFilter("iCalendar Files (.ics)", "ics"));
		
		String output = Biweekly.write(cal).go();
		
		try {
			if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				if(!file.toString().endsWith(".ics")) //enforce .ics file ending
					file = new File(file.toString() + ".ics");
				
				PrintWriter pw = new PrintWriter(file);
				pw.print(output);
				pw.close();
				JOptionPane.showMessageDialog(null, "Your file was succesfully exported. Find it at\n" + file.toString());
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "An error occurred in exporting to an ICal file. See the printed stack trace.");
			ex.printStackTrace();
		}
	}
}
