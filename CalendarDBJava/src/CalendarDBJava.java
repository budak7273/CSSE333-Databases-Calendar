import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CalendarDBJava extends JFrame {
    private static final int WINDOW_WIDTH = 1450;
    private static final int WINDOW_HEIGHT = 872;

    private AssignmentService assignmentService;
    private DatabaseConnectionService dbConnectService;
    private Container container;

    private MonthView monthView = new MonthView();
    private UserAccessControl userAccessControl = new UserAccessControl();

    private ImportHandler importHandler;
    private CalendarSharingHandler sharingHandler;

    private ArrayList<Assignment> assignmentList;

    private static final String SERVER_NAME = "golem.csse.rose-hulman.edu";
    private static final String DATABASE_NAME = "CalendarDB";

    public CalendarDBJava() {
        // Setting Swing and JFrame related properties.
        super("CalendarDB UI");
        container = getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(monthView.getMonthViewBackgroundColor());
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        userAccessControl.loginPrompt();
        monthView.setCurrentMonth();
        
        dbConnectService = new DatabaseConnectionService(SERVER_NAME, DATABASE_NAME);
        assignmentService = new AssignmentService(dbConnectService); //TODO close this connection eventually, which can probably happen earlier than window close
        
        importHandler = new ImportHandler(SERVER_NAME, DATABASE_NAME);
        sharingHandler = new CalendarSharingHandler(SERVER_NAME, DATABASE_NAME, userAccessControl.getUsername());

        // Close DB connection on exit
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dbConnectService.closeConnection();
                System.exit(0);
            }
        });

        //Buttons For Days
        JButton prevMonthButton = new JButton("Prev. Month");
        prevMonthButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                monthView.setPreviousMonth();
                repaint();
            }
        });
        prevMonthButton.setBounds(50,100,95,30);
        container.add(prevMonthButton);

        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                monthView.setCurrentMonth();
                repaint();
            }
        });
        todayButton.setBounds(50,100,95,30);
        container.add(todayButton);

        JButton nextMonthButton = new JButton("Next Month");
        nextMonthButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                monthView.setNextMonth();
                repaint();
            }
        });
        nextMonthButton.setBounds(50,100,95,30);
        container.add(nextMonthButton);

        JButton uploadIcalButton = new JButton("Upload iCal File");
        uploadIcalButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                importHandler.promptICalImport();
                assignmentList = assignmentService.getAllAssignmentsForUser(userAccessControl.getUsername()); // TODO maybe move db connection outside paint method?
                monthView.updateAssignmentList(assignmentList);
                paint(container.getGraphics());
            }
        });
        uploadIcalButton.setBounds(50,100,95,30);
        container.add(uploadIcalButton);

        JButton followCalendarButton = new JButton("Follow Calendar");
        followCalendarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.followCalendar();
                repaint();
            }

        });
        followCalendarButton.setBounds(50,100,95,30);
        container.add(followCalendarButton);

        JButton unfollowCalendarButton = new JButton("Unfollow Calendar");
        unfollowCalendarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.unfollowCalendar();
                repaint();
            }

        });
        unfollowCalendarButton.setBounds(50,100,95,30);
        container.add(unfollowCalendarButton);

        JButton listCalendarsButton = new JButton("List all Calendars");
        listCalendarsButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.listAllCalendars();
                repaint();
            }

        });
        listCalendarsButton.setBounds(50,100,95,30);
        container.add(listCalendarsButton);

        assignmentList = assignmentService.getAllAssignmentsForUser(userAccessControl.getUsername()); // TODO maybe move db connection outside paint method?
        monthView.updateAssignmentList(assignmentList);

        setVisible(true);   // Fixes buttons not rendering.
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        CalendarDBJava application = new CalendarDBJava();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        monthView.draw(g, getSize());
    }
}