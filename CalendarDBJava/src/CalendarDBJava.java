import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CalendarDBJava extends JFrame {
    private static final int WINDOW_WIDTH = 1700;
    private static final int WINDOW_HEIGHT = 1100;

    private AssignmentService assignmentService;
    private DatabaseConnectionService dbConnectService;
    private Container container;

    private MonthView monthView = new MonthView();
    private UpcomingEventsView upcomingEventsView = new UpcomingEventsView();
    private UserAccessControl userAccessControl;

    private ImportHandler importHandler;
    private CalendarSharingHandler sharingHandler;

    private static final String SERVER_NAME = "golem.csse.rose-hulman.edu";
    private static final String DATABASE_NAME = "CalendarDB";

    private enum View {
        MONTH_VIEW,
        UPCOMING_EVENTS_VIEW
    }

    private View currentView = View.MONTH_VIEW;


    public CalendarDBJava() {
        // Setting Swing and JFrame related properties.
        super("CalendarDB UI");
        container = getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(monthView.getMonthViewBackgroundColor());
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initializing various services
        dbConnectService = new DatabaseConnectionService(SERVER_NAME, DATABASE_NAME);

        userAccessControl = new UserAccessControl(dbConnectService);
        userAccessControl.startupPrompt();

        if (!userAccessControl.startupPrompt()) {   // User cancelled login/register screen
            System.exit(0);
        }
        sharingHandler = new CalendarSharingHandler(SERVER_NAME, DATABASE_NAME, userAccessControl.getUsername());
        assignmentService = new AssignmentService(dbConnectService, userAccessControl.getUsername(), sharingHandler);
        monthView.setCurrentMonth();
        
        importHandler = new ImportHandler(SERVER_NAME, DATABASE_NAME, userAccessControl.getUsername());
        

        // Close DB connection on exit
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dbConnectService.closeConnection();
                System.exit(0);
            }
        });

        addAllButtons();

        updateAndRedrawAssignments();
        setVisible(true);   // Fixes buttons not rendering.
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        CalendarDBJava application = new CalendarDBJava();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        switch (currentView) {
            case MONTH_VIEW:
                monthView.draw(g, getSize());
                break;
            case UPCOMING_EVENTS_VIEW:
                upcomingEventsView.draw(g, getSize());
        }
    }
    
    private void updateAndRedrawAssignments() {
        ArrayList<Assignment> assignmentList = assignmentService.getAllAssignments();
        monthView.updateAssignmentList(assignmentList);
        upcomingEventsView.updateAssignmentList(assignmentList);
        repaint();
    }

    private void addAllButtons() {
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

        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                userAccessControl.resetPasswordPrompt();
            }
        });
        resetPasswordButton.setBounds(50,100,95,30);
        container.add(resetPasswordButton);

        JButton uploadIcalButton = new JButton("Upload iCal File");
        uploadIcalButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                importHandler.promptICalImport();
                updateAndRedrawAssignments();
            }
        });
        uploadIcalButton.setBounds(50,100,95,30);
        container.add(uploadIcalButton);

        JButton followCalendarButton = new JButton("Join Calendar");
        followCalendarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.followCalendar();
                updateAndRedrawAssignments();
            }

        });
        followCalendarButton.setBounds(50,100,95,30);
        container.add(followCalendarButton);

        JButton unfollowCalendarButton = new JButton("Leave Calendar");
        unfollowCalendarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.unfollowCalendar();
                updateAndRedrawAssignments();
            }

        });
        unfollowCalendarButton.setBounds(50,100,95,30);
        container.add(unfollowCalendarButton);

        JButton listFollowedCalendarsButton = new JButton("Joined Calendars");
        listFollowedCalendarsButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.listAllFollowedCalendars(true);
                updateAndRedrawAssignments();
            }

        });
        listFollowedCalendarsButton.setBounds(50,100,95,30);
        container.add(listFollowedCalendarsButton);

        /*JButton listCalendarsButton = new JButton("List all Calendars");
		listCalendarsButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sharingHandler.getListAllCalendarsOverall(true);
                updateAndRedrawAssignments();
            }

        });
        listCalendarsButton.setBounds(50,100,95,30);
        container.add(listCalendarsButton);*/

        JButton exportButton = new JButton("Export Events");
        exportButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                ExportHandler.convertAssignmentsToICal(assignmentService.getAllAssignments());
            }

        });
        exportButton.setBounds(50,100,95,30);
        container.add(exportButton);

        JButton createNewAssignmentButton = new JButton("Add Assignment");
        createNewAssignmentButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (assignmentService.createNewAssignmentPrompt()) {
                    updateAndRedrawAssignments();
                }
            }
        });
        createNewAssignmentButton.setBounds(50,100,95,30);
        container.add(createNewAssignmentButton);
    }
}