import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpcomingEventsView extends View {
    JButton filterButton;

    private Font assignmentFont = new Font("Helvetica", Font.PLAIN, 15);
    private Font dateFont = new Font("Helvetica", Font.PLAIN, 30);
    private Color assignmentListBackgroundColor = Color.DARK_GRAY;
    private Color viewBackgroundColor = new Color(0x008057);

    private int assignmentListWidth = 1000;
    private int assignmentListHeight = 800;

    private int topMargin = 40 + 22 + dateFont.getSize()  + 10;     // + 22 accounts for Window Bar, + mFT.gS accounts
                                                                    // for today's date title
    private int bottomMargin = 25;

    private int leftMargin = 25;
    private int rightMargin = 25;

    private int centerMargin = 25;

    private int assignmentHeight = 35;
    private int assignmentWidth = 1000;
    private int assignmentVerticalSeparation = 5;
    private int assignmentLeftMargin = 5;
    private int assignmentRightMargin = 5;

    private int assignmentTextLengthLimit = 60;

    private int progressBarThickness = 7;


    private Color monthViewDateColor = Color.WHITE;
    private int eventHeight;
    private Graphics g;
    private int assignmentDisplayLimit = 3;

    public void draw(Graphics g, Dimension d) {
        updateWindowDimensions(d.width, d.height);
        draw(g);
    }

    public void updateWindowDimensions(int width, int height) {
        this.assignmentListWidth = (width - leftMargin - rightMargin);
        this.assignmentListHeight = height - topMargin - bottomMargin;

        this.assignmentWidth = assignmentListWidth - (assignmentLeftMargin + assignmentRightMargin);

        assignmentDisplayLimit = assignmentListHeight / (assignmentHeight + assignmentVerticalSeparation);
    }

    public void draw(Graphics g) {
        this.g = g;

        System.out.print("Drawing UpcomingAssignmentsView... ");

        // Month background
        g.setColor(assignmentListBackgroundColor);
        g.fillRect(leftMargin, topMargin, assignmentListWidth, assignmentListHeight);

        // Month Title
        Calendar cal = Calendar.getInstance();
//        cal.set(year, month, 1);
        g.setColor(Color.WHITE);
        g.setFont(dateFont);
        g.drawString("Today: " + new SimpleDateFormat("MMMM d, YYYY").format(cal.getTime()), leftMargin + 5,
                topMargin - 10);

        assignmentLocationList.clear();

        int assignmentsDisplayed = 0;
        Date minDate = new Date();
        for (Assignment a : assignmentList) {
            if (a.getEventDate().compareTo(minDate) >= 0) { // Is an upcoming assignment :)
                drawAssignment(a, assignmentsDisplayed++);
            }
            if (assignmentsDisplayed > assignmentDisplayLimit) {
                break;
            }
        }
        System.out.printf("Drew %d assignments\n", assignmentsDisplayed);
    }

    private void drawAssignment(Assignment a, int i) {
        // Calculate x & y
        int x = leftMargin + assignmentLeftMargin;
        int y = i * (assignmentHeight + assignmentVerticalSeparation) + topMargin + assignmentVerticalSeparation;

        // Store assignment and position for processing clicks.
        assignmentLocationList.add(new AssignmentWrapper(a, x, y, assignmentWidth, assignmentHeight));

        // Set event color
        Color assignmentColor = new Color(a.getEventSpecificColor());
        g.setColor(assignmentColor);
        g.fillRect(x, y, assignmentWidth, assignmentHeight);

        // Format Assignment String and cut off if exceeds limit.
        String assignmentDateStr = new SimpleDateFormat("MMMM d, YYYY").format(a.getEventDate());
        String assignmentText = String.format("%s: %s", assignmentDateStr, a.getEventName());
        assignmentText = assignmentText.length() > assignmentTextLengthLimit ? assignmentText.substring(0, assignmentTextLengthLimit) + "..." : assignmentText;

        // Determine text color
        float colorBrightness = Color.RGBtoHSB(assignmentColor.getRed(), assignmentColor.getGreen(),
                assignmentColor.getBlue(), null)[2];
        g.setColor(colorBrightness > .8f ? Color.BLACK : Color.WHITE);

        // Display Text
        g.setFont(assignmentFont);
        g.drawString(assignmentText, x + 5, y + 15);

        // Show Progress Bar
        int progressBarY = y + assignmentHeight - progressBarThickness;

        g.setColor(Color.BLACK);
        g.fillRect(x, progressBarY, assignmentWidth, progressBarThickness);
        g.setColor(Color.WHITE);
        g.fillRect(x, progressBarY, assignmentWidth * a.getEventProgress() / 100, progressBarThickness);
    }

    private boolean filterPrompt() {
        return true;
    }



    @Override
    public Color getViewBackgroundColor() {
        return viewBackgroundColor;
    }

    @Override
    public void addViewButtons(CalendarDBJava calDB) {
        filterButton = new JButton("Filter");
        filterButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (calDB.getUpcomingEventsView().filterPrompt()) {
                    calDB.updateAndRedrawAssignments();
                }
            }
        });
        filterButton.setBounds(50,100,95,30);
        calDB.getContainer().add(filterButton);
    }

    @Override
    public void showViewButtons(CalendarDBJava calDB) {
        filterButton.show();
    }

    @Override
    public void hideViewButtons(CalendarDBJava calDB) {
        filterButton.hide();
    }
}
