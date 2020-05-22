import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthView {
    private Font dayOfMonthFont = new Font("Helvetica", Font.PLAIN, 15);
    private Color dayColor = Color.DARK_GRAY;
    private Color monthViewBorderColor = Color.BLACK;
    private Color monthViewBackgroundColor = new Color(0x800000);
    private Color monthViewDateColor = Color.WHITE;
    private int monthWidth = 1400;
    private int monthHeight = 800;
    private int leftMargin = 25;
    private int rightMargin = 25;
    private int topMargin = 40 + 22;     // + 22 accounts for Window Bar.
    private int bottomMargin = 25;
    private int horizontalDaySeparation = 5;
    private int verticalDaySeparation = 5;
    private int eventVerticalSeparation = 4;
    private int dayWidth;
    private int dayHeight;
    private Graphics g;
    private ArrayList<Assignment> assignmentList;
    private int maxEventsDisplayed = 3;
    private int firstDayOfMonth;
    private int daysInMonth;
    private int weeksInMonth;
    private int year;
    private int month;

    public void updateAssignmentList(ArrayList<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public MonthView() {

    }

    public void updateWindowDimensions(int width, int height) {
        this.monthWidth = width - leftMargin - rightMargin;
        this.monthHeight = height - topMargin - bottomMargin;

        this.dayHeight = (this.monthHeight - this.verticalDaySeparation) / this.weeksInMonth;
        this.dayWidth = (this.monthWidth - this.horizontalDaySeparation) / 7;
    }

    /**
     * Draws the current (IRL) month.
     */
    public void setCurrentMonth() {
        Date currentDate = new Date();
        setMonth(currentDate.getYear() + 1900, currentDate.getMonth());
    }

    /**
     * Advances to the next month. Must call setCurrentMonth(g) or setMonth(year, month) first if you dont want to start
     * in 0 AD...
     */
    public void setNextMonth() {
        int nextMonth = month + 1;
        int nextMonthsYear = year;
        if (nextMonth > Calendar.DECEMBER) {
            nextMonth = Calendar.JANUARY;
            nextMonthsYear++;
        }
        setMonth(nextMonthsYear, nextMonth);
    }

    /**
     * Advances to the previous month. Must call setCurrentMonth(g) or setMonth(year, month) first if you dont want to start
     * in 0 AD...
     */
    public void setPreviousMonth() {
        int prevMonth = month - 1;
        int prevMonthsYear = year;
        if (prevMonth < Calendar.JANUARY) {
            prevMonth = Calendar.DECEMBER;
            prevMonthsYear--;
        }
        setMonth(prevMonthsYear, prevMonth);
    }

    /**
     * Draws the currently stored month, and updates the window size.
     * @param g the graphics window to draw on
     * @param d the window dimensions
     */
    public void draw(Graphics g, Dimension d) {
        updateWindowDimensions(d.width, d.height);
        draw(g);
    }

    /**
     * Draws the currently stored month. setCurrentMonth() or setMonth(year, month) should probably be called before calling this.
     * @param g the graphics window to draw on
     */
    public void draw(Graphics g) {
        this.g = g;

        System.out.printf("Drawing month %d, year %d\n", month, year);

        g.setColor(monthViewBorderColor);
        g.fillRect(leftMargin, topMargin, monthWidth, monthHeight);

        int dayOfMonth = 1;
        int j = firstDayOfMonth - 1;
        for (int i = 0; i < weeksInMonth; i++) {
            for (; j < 7; j++) {
                int x = leftMargin + horizontalDaySeparation + dayWidth * j;
                int y = topMargin + verticalDaySeparation + dayHeight * i;
                drawDay(x, y, dayOfMonth);

                dayOfMonth++;
                if (dayOfMonth > daysInMonth) {
                    return;
                }
            }
            j = 0;
        }
    }

    /**
     * Sets the stored month to specified value.
     * @param year the year of the month to draw (in normal format, ex 2020)
     * @param month the month (zero indexed, like Calendar.MONTH_NAME)
     */
    public void setMonth(int year, int month) {
        this.year = year;
        this.month = month;
        updateMonthProperties();
    }

    private void drawDay(int x,  int y, int dayOfMonth) {
        // Day Rectangle
        g.setColor(dayColor);
        g.fillRect(x, y, dayWidth - horizontalDaySeparation, dayHeight - verticalDaySeparation);

        // Date Label
        g.setColor(monthViewDateColor);
        g.setFont(dayOfMonthFont);
        g.drawString(Integer.toString(dayOfMonth), x + horizontalDaySeparation, y + dayOfMonthFont.getSize() + verticalDaySeparation);

        drawEventList(x, y, dayOfMonth);
    }

    private void drawEventList(int x, int y, int dayOfMonth) {
        int eventsDisplayed = 0;
        int eventHeight = dayHeight / (maxEventsDisplayed + 1);
        for (Assignment a : assignmentList) {
            if (a.getEventDate().getYear() == year - 1900 && a.getEventDate().getMonth() == month && a.getEventDate().getDate() == dayOfMonth) {
                drawAssignment(a, x + 5, y + 30 + (eventHeight) * eventsDisplayed, dayWidth - 15, eventHeight - eventVerticalSeparation);

                eventsDisplayed++;
                if (eventsDisplayed >= maxEventsDisplayed) return;
            }
        }
    }

    private void drawAssignment(Assignment assignment, int x, int y, int width, int height) {
        Color color = new Color(assignment.getEventSpecificColor());
        float colorBrightness = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[2];

        g.setColor(color);
        g.fillRect(x, y, width, height);

        String eventName = assignment.getEventName();
        eventName = eventName.length() > 22 ? eventName.substring(0, 22) + "..." : eventName;

        g.setColor(colorBrightness > .8f ? Color.BLACK : Color.WHITE);
        g.drawString(eventName, x + horizontalDaySeparation, y + 20);

        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, 4);
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width * assignment.getEventProgress() / 100, 4);
    }

    /**
     * Updates the month's length, number of weeks, and start day based on the stored month and year values.
     */
    private void updateMonthProperties() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);

        firstDayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        daysInMonth = cal.getActualMaximum(Calendar.DATE);
        weeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    public Color getMonthViewBackgroundColor() {
        return monthViewBackgroundColor;
    }
}
