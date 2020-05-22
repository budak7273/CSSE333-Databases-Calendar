import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CalendarDBJava extends JFrame {
    private static final int WINDOW_WIDTH = 1700;
    private static final int WINDOW_HEIGHT = 1100;

    private AssignmentService assignmentService;
    private DatabaseConnectionService dbConnectService;
    private Container container;

    private MonthView monthView = new MonthView();
    private UpcomingEventsView upcomingEventsView = new UpcomingEventsView();
    private View displayedView = monthView;
    private UserAccessControl userAccessControl;

    private ImportHandler importHandler;
    private CalendarSharingHandler sharingHandler;
    private ClassSectionService classSectionService;

    private static final String SERVER_NAME = "golem.csse.rose-hulman.edu";
    private static final String DATABASE_NAME = "CalendarDB";

    public CalendarDBJava() {
        // Setting Swing and JFrame related properties.
        super("CalendarDB UI");
        container = getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(displayedView.getViewBackgroundColor());
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initializing various services
        dbConnectService = new DatabaseConnectionService(SERVER_NAME, DATABASE_NAME);

        userAccessControl = new UserAccessControl(dbConnectService);

        if (!userAccessControl.startupPrompt()) {   // User cancelled login/register screen
            System.exit(0);
        }
        classSectionService = new ClassSectionService(dbConnectService, userAccessControl.getUsername());
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

        // Click Listener for Viewing/Editing Events in UpcomingEventsView
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                displayedView.processClick(e);
            }
        });

        addSharedButtons();

        updateAndRedrawAssignments();

        setVisible(false);  // Fixes buttons not rendering.
        setVisible(true);
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        CalendarDBJava application = new CalendarDBJava();
        application.addViewButtons();
    }

    private void addViewButtons() {
        monthView.addViewButtons(this);
        monthView.hideViewButtons(this);
        upcomingEventsView.addViewButtons(this);
        upcomingEventsView.hideViewButtons(this);

        displayedView.setCalendarDBJava(this);
        displayedView.showViewButtons(this);

        container.setVisible(false);
        container.setVisible(true);
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        displayedView.draw(g, getSize());
    }
    
    public void updateAndRedrawAssignments() {
        ArrayList<Assignment> assignmentList = assignmentService.getAllAssignmentsSortedByDate();
        displayedView.updateAssignmentList(assignmentList);
        container.setBackground(displayedView.getViewBackgroundColor());
        repaint();
    }

    private void addSharedButtons() {
        CalendarDBJava myThis = this;
        JButton switchViewButton = new JButton("Switch View");
        switchViewButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                myThis.swapViews();
                updateAndRedrawAssignments();
            }
        });
        switchViewButton.setBounds(50,100,95,30);
        container.add(switchViewButton);


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

        JButton newClassSectionButton = new JButton("New Class Section");
        newClassSectionButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                classSectionService.newClassSectionPrompt();
            }
        });
        newClassSectionButton.setBounds(50,100,95,30);
        container.add(newClassSectionButton);

        JButton deleteClassSectionButton = new JButton("Delete Class Section");
        deleteClassSectionButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                classSectionService.deleteClassSectionPrompt();
            }
        });
        deleteClassSectionButton.setBounds(50,100,95,30);
        container.add(deleteClassSectionButton);

        JButton joinClassSectionButton = new JButton("Join Class Section");
        joinClassSectionButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                classSectionService.joinClassSectionPrompt();
            }
        });
        joinClassSectionButton.setBounds(50,100,95,30);
        container.add(joinClassSectionButton);

        JButton leaveClassSectionButton = new JButton("Leave Class Section");
        leaveClassSectionButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                classSectionService.leaveClassSectionPrompt();
            }
        });
        leaveClassSectionButton.setBounds(50,100,95,30);
        container.add(leaveClassSectionButton);
    }

    private void swapViews() {
        displayedView.hideViewButtons(this);
        if (monthView.equals(displayedView)) {
            displayedView = upcomingEventsView;
        } else if (upcomingEventsView.equals(displayedView)) {
            displayedView = monthView;
        }
        displayedView.showViewButtons(this);
        container.setVisible(false);
        container.setVisible(true);
    }

    public Container getContainer() {
        return container;
    }

    public MonthView getMonthView() {
        return monthView;
    }

    public UpcomingEventsView getUpcomingEventsView() {
        return upcomingEventsView;
    }

    public CalendarSharingHandler getSharingHandler() {
        return sharingHandler;
    }

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }
}