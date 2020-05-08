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

    public MonthView(ArrayList<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    /**
     * Draws the current (IRL) month.
     */
    public void drawMonth(Graphics g) {
        Date currentDate = new Date();
        drawMonth(g, currentDate.getYear(), currentDate.getMonth());
    }

    /**
     * Draws a given Month.
     * @param year the year of the month to draw (in normal format, ex 2020)
     * @param month the month (zero indexed, like Calendar.MONTH_NAME)
     */
    public void drawMonth(Graphics g, int year, int month) {
        this.g = g;
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
        int eventsDisplayed = 0;
        int eventHeight = dayHeight / (maxEventsDisplayed + 1);
        for (Assignment a : assignmentList) {
            if (a.getEventDate().getYear() == year - 1900 && a.getEventDate().getMonth() == month && a.getEventDate().getDate() == dayOfMonth) {
                drawAssignment(a, x + 5, y + 30 + eventHeight * eventsDisplayed, dayWidth - 15, eventHeight);
                eventsDisplayed++;
                if (eventsDisplayed > maxEventsDisplayed) return;
            }
        }
    }

    private void drawAssignment(Assignment assignment, int x, int y, int width, int height) {
        g.setColor(new Color(assignment.getEventSpecificColor()));
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString(assignment.getEventName(), x + horizontalDaySeparation, y + 20);
    }

    private void updateMonth() {
//        Date firstDayOfMonthDate = new Date(year, month, 1);
//        firstDayOfMonth = firstDayOfMonthDate.getDay();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        firstDayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        daysInMonth = cal.getActualMaximum(Calendar.DATE);
    }
}
