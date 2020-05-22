import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UpcomingEventsView {
    private Font assignmentFont = new Font("Helvetica", Font.PLAIN, 15);
    private Font dateFont = new Font("Helvetica", Font.PLAIN, 30);
    private Color monthViewBorderColor = Color.BLACK;
    private Color monthViewBackgroundColor = new Color(0x800000);
    private Color monthViewDateColor = Color.WHITE;
    private int monthWidth = 1400;
    private int monthHeight = 800;
    private int leftMargin = 25;
    private int rightMargin = 25;
    private int topMargin = 40 + 22 + dateFont.getSize()  + 10;     // + 22 accounts for Window Bar, + mFT.gS accounts for month title
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


    private ArrayList<AssignmentWrapper> assignmentLocationList = new ArrayList<>();


    public void draw(Graphics g, Dimension size) {

    }

    public void updateAssignmentList(ArrayList<Assignment> allAssignments) {

    }

    public void processClick(MouseEvent e) {
        System.out.printf("UEV: Processing mouse click at x=%d, y=%d...", e.getX(), e.getY());

        for (AssignmentWrapper aw : assignmentLocationList) {
            if(aw.contains(e)) {
                editMenu(aw.assignment);
            }
        }
    }

    private void editMenu(Assignment assignment) {

    }

    private class AssignmentWrapper {
        Assignment assignment;
        int x;
        int y;
        int width;
        int height;

        public boolean contains(MouseEvent e) {
            int eX = e.getX();
            int eY = e.getY();
            boolean validX = eX >= x  && eX <= x + width;
            boolean validY = eY >= y  && eY <= y + height;
            return validX && validY;
        };
    }
}
