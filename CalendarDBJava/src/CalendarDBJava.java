import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarDBJava extends JFrame {
    private AssignmentService assignmentService;
    private DatabaseConnectionService dbConnectService;
    private ImportHandler importHandler;
    private Container mContainer;

    private Font dayOfMonthFont = new Font("Helvetica", Font.PLAIN, 15);
    private Color monthViewDayColor = Color.BLACK;
    private Color monthViewBorderColor = Color.DARK_GRAY;
    private Color monthViewBackgroundColor = new Color(0x0C1C6F);
    private Color monthViewDayOfMonthColor = Color.WHITE;
    private ArrayList<Assignment> assignmentList;

    private String username;
    private String password; //security vulnerability to store password in memory
    
    private static final boolean DEBUG_LOGIN = true; //flip this to true to not have to enter login credentials (for testing)
    
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
        
        mContainer = getContentPane();
        mContainer.setLayout(new FlowLayout());
        mContainer.setBackground(monthViewBackgroundColor);
        setSize(1450, 872);
        setVisible(true);
        
        dbConnectService = new DatabaseConnectionService("golem.csse.rose-hulman.edu", "CalendarDB");
        importHandler = new ImportHandler(dbConnectService);
        
        //close DB connection on exit
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
        assignmentService = new AssignmentService(dbConnectService);
        assignmentList = assignmentService.getAllAssignmentsForUser(username, password); //TODO
        drawMonthView(g, 1400, 800, 25, 25+22, 5, 5, 3, 30);
        importHandler.promptICalImport();
    }

    private void drawMonthView(Graphics g, int monthWidth, int monthHeight, int leftOffset, int topOffset,
                               int dayVerticalSeparation, int dayHorizontalSeparation, int firstDayOfMonth, int daysInMonth) {
        g.setColor(monthViewBorderColor);
        g.fillRect(leftOffset, topOffset, monthWidth, monthHeight);
        leftOffset += dayHorizontalSeparation;
        topOffset += dayVerticalSeparation;
        int dayWidth = (monthWidth - dayHorizontalSeparation/* - 6 * dayHorizontalSeparation*/) / 7;
        int dayHeight = (monthHeight - dayVerticalSeparation) / 5;
        int dayOfMonth = 1;
        int j = firstDayOfMonth;
        for (int i = 0; i < 5; i++) {
            for (; j < 7; j++) {
                g.setColor(monthViewDayColor);
                int x = leftOffset + dayWidth * j;
                int y = topOffset + dayHeight * i;
                g.fillRect(x, y, dayWidth - dayHorizontalSeparation, dayHeight - dayVerticalSeparation);

                g.setColor(monthViewDayOfMonthColor);
                g.setFont(dayOfMonthFont);
                g.drawString(Integer.toString(dayOfMonth), x + dayHorizontalSeparation, y + dayOfMonthFont.getSize() + dayHorizontalSeparation);

                drawDayEventListMonthView(g, x, y, dayWidth, dayHeight, 3, dayOfMonth);

                dayOfMonth++;
                if (dayOfMonth > daysInMonth) {
                    i = 10;
                    j = 10;
                }
            }
            j = 0;
        }
    }

    private void drawDayEventListMonthView(Graphics g, int x, int y, int width, int height, int maxEventsDisplayed, int dayOfMonth) {
        for (Assignment a : assignmentList) {
            //System.out.println(a);
            if (a.getEventDate().getYear() == 2020-1900 && a.getEventDate().getMonth() == 5-1 && a.getEventDate().getDate() == dayOfMonth)
                drawAssignment(g, a, x + 5, y + 30, width - 15, height / maxEventsDisplayed);
        }
    }

    private void drawAssignment(Graphics g, Assignment assignment, int x, int y, int width, int height) {
        g.setColor(new Color(assignment.getEventSpecificColor()));
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString(assignment.getEventName(), x, y + 20);
    }

    


}










