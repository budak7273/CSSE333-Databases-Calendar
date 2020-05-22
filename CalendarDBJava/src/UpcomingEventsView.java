import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpcomingEventsView extends View {
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

        System.out.print("Drawing Upcoming Assignments View... ");

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
        // Calculate x & y and store for evaluating clicks.
        int x = leftMargin + assignmentLeftMargin;
        int y = i * (assignmentHeight + assignmentVerticalSeparation) + topMargin + assignmentVerticalSeparation;
        assignmentLocationList.add(new AssignmentWrapper(a, x, y, assignmentWidth, assignmentHeight));

        Color assignmentColor = new Color(a.getEventSpecificColor());
        g.setColor(assignmentColor);
        g.fillRect(x, y, assignmentWidth, assignmentHeight);

        String eventName = a.getEventName();
        eventName = eventName.length() > 60 ? eventName.substring(0, 60) + "..." : eventName;

        float colorBrightness = Color.RGBtoHSB(assignmentColor.getRed(), assignmentColor.getGreen(),
                assignmentColor.getBlue(), null)[2];
        g.setColor(colorBrightness > .8f ? Color.BLACK : Color.WHITE);
        g.setFont(assignmentFont);
        g.drawString(eventName, x + 5, y + 15);
    }





    @Override
    public Color getViewBackgroundColor() {
        return viewBackgroundColor;
    }
}
