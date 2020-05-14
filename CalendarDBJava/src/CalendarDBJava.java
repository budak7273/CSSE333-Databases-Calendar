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
    private CalendarSharingHandler sharingHandler;

    private ArrayList<Assignment> assignmentList;

    //
    private String username;
    private String password;
    
    private static final String SERVER_NAME = "golem.csse.rose-hulman.edu";
    private static final String DATABASE_NAME = "CalendarDB";
    

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

        dbConnectService = new DatabaseConnectionService(SERVER_NAME, DATABASE_NAME);
        assignmentService = new AssignmentService(dbConnectService); //TODO close this connection eventually, which can probably happen earlier than close
        
        importHandler = new ImportHandler(SERVER_NAME, DATABASE_NAME);
        sharingHandler = new CalendarSharingHandler(SERVER_NAME, DATABASE_NAME, username);

        JButton uploadiCalButton=new JButton("Upload iCal File");
        uploadiCalButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                importHandler.promptICalImport();
                redraw();
            }

        });
        uploadiCalButton.setBounds(50,100,95,30);
        container.add(uploadiCalButton);
        
        JButton followCalendarButton =new JButton("Follow Calendar");
        followCalendarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	sharingHandler.followCalendar();
                redraw();
            }

        });
        followCalendarButton.setBounds(50,100,95,30);
        container.add(followCalendarButton);
        
        JButton unfollowCalendarButton =new JButton("Unfollow Calendar");
        unfollowCalendarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	sharingHandler.unfollowCalendar();
                redraw();
            }

        });
        unfollowCalendarButton.setBounds(50,100,95,30);
        container.add(unfollowCalendarButton);
        
        JButton listCalendarsButton =new JButton("List all Calendars");
        listCalendarsButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	sharingHandler.listAllCalendars();
                redraw();
            }

        });
        listCalendarsButton.setBounds(50,100,95,30);
        container.add(listCalendarsButton);

        assignmentList = assignmentService.getAllAssignmentsForUser(username); // TODO maybe move db connection outside paint method?
        monthView = new MonthView(assignmentList);

        // Close DB connection on exit
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	dbConnectService.closeConnection();
            	System.exit(0);
            }
        });

        setVisible(true);
    }

    private void redraw() {
    	assignmentList = assignmentService.getAllAssignmentsForUser(username); // TODO maybe move db connection outside paint method?
        monthView = new MonthView(assignmentList);
        paint(container.getGraphics());
    }
    
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        CalendarDBJava application = new CalendarDBJava();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        monthView.drawMonth(g, 2020, Calendar.MAY);
    }

}










