import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthView {
    private Font dayOfMonthFont = new Font("Helvetica", Font.PLAIN, 15);
    private Color dayColor = Color.BLACK;
    private Color monthViewBorderColor = Color.DARK_GRAY;
    private Color monthViewBackgroundColor = new Color(0x0C1C6F);
    private Color monthViewDateColor = Color.WHITE;
    private int monthWidth = 1400;
    private int monthHeight = 800;
    private int leftMargin = 25;
    private int rightMargin = 25;
    private int topMargin = 25 + 22;     // + 22 accounts for Window Bar.
    private int bottomMargin = 25;
    private int horizontalDaySeparation = 5;
    private int verticalDaySeparation = 5;
    private int dayWidth = (monthWidth - horizontalDaySeparation) / 7;
    private int dayHeight = (monthHeight - verticalDaySeparation) / 5;
    private Graphics g;
    private ArrayList<Assignment> assignmentList;
    private int maxEventsDisplayed = 3;
    private int firstDayOfMonth;
    private int daysInMonth;
    private int year;
    private int month;

    public MonthView(Graphics g, ArrayList<Assignment> assignmentList) {
        this.g = g;
        this.assignmentList = assignmentList;
    }

    /**
     * Draws the current (IRL) month.
     */
    public void drawMonth() {
        drawMonth(2020, Calendar.MAY);
    }

    /**
     * Draws a given Month.
     * @param year the year of the month to draw.
     * @param month the month (zero indexed, like Calendar.MONTH_NAME)
     */
    public void drawMonth(int year, int month) {
        this.year = year;
        this.month = month;
        updateMonth();

        g.setColor(monthViewBorderColor);
        g.fillRect(leftMargin, topMargin, monthWidth, monthHeight);

        int dayOfMonth = 1;
        int j = firstDayOfMonth - 1;
        for (int i = 0; i < 5; i++) {
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
        for (Assignment a : assignmentList) {
            if (a.getEventDate().getYear() == 2020-1900 && a.getEventDate().getMonth() == month && a.getEventDate().getDate() == dayOfMonth)
                drawAssignment(a, x + 5, y + 30, dayWidth - 15, dayHeight / maxEventsDisplayed);
        }
    }

    private void drawAssignment(Assignment assignment, int x, int y, int width, int height) {
        g.setColor(new Color(assignment.getEventSpecificColor()));
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString(assignment.getEventName(), x, y + 20);
    }

    private void updateMonth() {
        if (year == 2020 && month == Calendar.MAY) {
            firstDayOfMonth = Calendar.FRIDAY;
            daysInMonth = 31;
        }
    }

}
