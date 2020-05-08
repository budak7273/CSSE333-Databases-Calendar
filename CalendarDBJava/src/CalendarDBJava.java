import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarDBJava extends JFrame {
    // TODO: flip this to true to not have to enter login credentials (for testing)
    private static final boolean DEBUG_LOGIN = true;
    private static final int WINDOW_WIDTH = 1450;
    private static final int WINDOW_HEIGHT = 872;

    private AssignmentService assignmentService;
    private DatabaseConnectionService dbConnectService;
    private Container container;

    private MonthView monthView;

    private ImportHandler importHandler;

    private ArrayList<Assignment> assignmentList;

    //
    private String username;
    private String password;
    

    public CalendarDBJava() {
        super("CalendarDB UI");

        //User login
        if(DEBUG_LOGIN) {
        	username = "DemoUser";
        	password = "DemoPass";
        } else {
        	username = JOptionPane.showInputDialog("Enter your username.");
        	password = JOptionPane.showInputDialog("Enter your password.");
        }
        
        container = getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(Color.BLUE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        
        dbConnectService = new DatabaseConnectionService("golem.csse.rose-hulman.edu", "CalendarDB");
        assignmentService = new AssignmentService(dbConnectService); //TODO close this connection eventually, which can probably happen earlier than close
        
        
        importHandler = new ImportHandler("golem.csse.rose-hulman.edu", "CalendarDB");

        JButton b=new JButton("Upload iCal File");
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                importHandler.promptICalImport();
            }

        });
        b.setBounds(50,100,95,30);
        container.add(b);


        // Close DB connection on exit
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	dbConnectService.closeConnection();
            	System.exit(0);
            }
        });
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        CalendarDBJava application = new CalendarDBJava();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        assignmentList = assignmentService.getAllAssignmentsForUser(username); // TODO maybe move db connection outside paint method?
        monthView = new MonthView(g, assignmentList);
        monthView.drawMonth(2020 - 1900, Calendar.MAY);
    }

}










