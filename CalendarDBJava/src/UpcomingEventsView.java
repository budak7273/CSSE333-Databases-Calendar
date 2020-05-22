import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

    private Date filterStartDate = new Date();
    private Date filterEndDate = new Date(3000, 1, 1);  // Queue Jonas Brothers...
    private String filterEventName = "";
    private String filterEventType = "";
    private Integer filterClassCalendarID = null;
    private int filterMinProgress = 0;
    private int filterMaxProgress = 100;

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
        for (Assignment a : assignmentList) {
            if (assignmentMeetsFilters(a)) {
                drawAssignment(a, assignmentsDisplayed++);
            }
            if (assignmentsDisplayed > assignmentDisplayLimit) {
                break;
            }
        }
        System.out.printf("Drew %d assignments\n", assignmentsDisplayed);
    }

    private boolean assignmentMeetsFilters(Assignment a) {
        if (a == null) return false;
        if (a.getEventDate().compareTo(filterStartDate) < 0) return false;  // Before Start Date
        if (a.getEventDate().compareTo(filterEndDate) > 0) return false;    // After End Date
        if (a.getEventProgress() < filterMinProgress) return false;
        if (a.getEventProgress() > filterMaxProgress) return false;
        if (a.getEventName() != null && !a.getEventName().contains(filterEventName)) return false;
        if (a.getEventType() != null && !a.getEventType().contains(filterEventType)) return false;
        if (filterClassCalendarID != null && a.getParentClassCalendarID() != filterClassCalendarID) return false;

        return true;
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

    /**
     * Prompts the user for filter options.
     * @return true if filter was entered, false if cancelled.
     */
    private boolean filterPrompt(CalendarDBJava calDB) {
        JOptionPane.showMessageDialog(null, "Leave options as default to ignore filter");

        String startDateString = JOptionPane.showInputDialog("Enter Desired Start Date. ", new SimpleDateFormat("MMMM d, YYYY").format(new Date()));
        try {
            filterStartDate = new Date(startDateString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Start Date not in specified format.\nPlease try again.");
            return false;
        }

        String endDateString = JOptionPane.showInputDialog("Enter Desired End Date. ", new SimpleDateFormat("MMMM d, YYYY").format(new Date(3000, 1, 1)));
        try {
            filterEndDate = new Date(endDateString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "End Date not in specified format.\nPlease try again.");
            return false;
        }

        filterEventName = JOptionPane.showInputDialog("Enter Event Name Keyword", "");

        filterEventType = JOptionPane.showInputDialog("Enter Event Type\n(\"Homework\", \"Test\", etc)", "");

        String minProgressString = JOptionPane.showInputDialog("Minimum progress. ", filterMinProgress);
        try {
            filterMinProgress = Integer.parseInt(minProgressString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Min Progress not in specified format.\nPlease try again.");
            return false;
        }

        String maxProgressString = JOptionPane.showInputDialog("Maximum progress. ", filterMaxProgress);
        try {
            filterMaxProgress = Integer.parseInt(maxProgressString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Max Progress not in specified format.\nPlease try again.");
            return false;
        }


        HashMap<String, Integer> mapping = calDB.getSharingHandler().getFriendlyStringIDMapping(calDB.getSharingHandler().listAllFollowedCalendars(false));
        String[] choices = {};
        String calendarIDString = null;
        try {
            calendarIDString = JOptionPane.showInputDialog(null, "Select Calendar Filter By (Hit Cancel to skip)", "Calendar", JOptionPane.QUESTION_MESSAGE, null, mapping.keySet().toArray(choices), 1).toString();
        } catch (Exception ignored) {
            //do nothing, we don't care
        }
        if (calendarIDString == null) {
            filterClassCalendarID = null;    // indicates no selection (no class filter).
        } else {
            filterClassCalendarID = mapping.get(calendarIDString);
        }

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
                if (calDB.getUpcomingEventsView().filterPrompt(calDB)) {
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
