import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class View {

    protected static ArrayList<Assignment> assignmentList;
    protected ArrayList<AssignmentWrapper> assignmentLocationList = new ArrayList<>();






    public abstract void draw(Graphics g, Dimension d);


    public void updateAssignmentList(ArrayList<Assignment> allAssignments) {
        assignmentList = allAssignments;
    }

    public void processClick(MouseEvent e) {
        System.out.printf("UEV: Processing mouse click at x=%d, y=%d... ", e.getX(), e.getY());

        for (AssignmentWrapper aw : assignmentLocationList) {
            if(aw.contains(e)) {
                assignmentDetailWindow(aw.assignment);
                return;
            }
        }
        System.out.println("Click was outside of any assignment.");
    }

    protected void assignmentDetailWindow(Assignment assignment) {
        System.out.printf("Displaying Detail Window for assignment %s (ID = %d)\n", assignment.getEventName(),
                assignment.getAssignmentID());

        String body = String.format(
                "%s\n" +
                        "%s%% complete\n" +
                        "Due: %s\n" +
                        "Type: %s\n" +
                        "Class Section ID: %d\n" +
                        "Description:\n%s", assignment.getEventName(), assignment.getEventProgress(),
                assignment.getEventDate().toLocaleString(), assignment.getEventType() == null ? "" : assignment.getEventType(),
                assignment.getParentClassCalendarID(), assignment.getEventDescription());

        int selection = JOptionPane.showOptionDialog(null,
                body,
                assignment.getEventName(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {"Close", "Edit", "Delete"},
                "Close");
        switch (selection) {
            case 0:
                return;
            case 1:
                //TODO edit event
                break;
            case 2:
                //TODO delete event
                break;
        }
    }

    public abstract void addViewButtons(CalendarDBJava calDB);

    public abstract void showViewButtons(CalendarDBJava calDB);

    public abstract void hideViewButtons(CalendarDBJava calDB);

    public abstract Color getViewBackgroundColor();


    /**
     * Class to bundle an assignment with its bounding box (for tracking clicks)
     */
    protected class AssignmentWrapper {
        public AssignmentWrapper(Assignment assignment, int x, int y, int width, int height) {
            this.assignment = assignment;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Assignment assignment;
        int x;
        int y;
        int width;
        int height;

        /**
         * Method to determine if a click on a specific assignment.
         * @param e the mouse event to check.
         * @return true if the assignment was the target, false otherwise.
         */
        public boolean contains(MouseEvent e) {
            int eX = e.getX();
            int eY = e.getY() + 22;     // returned y value doesnt seem to factor in toolbar height, while the graphics
            // y does??
            boolean validX = eX >= x  && eX <= x + width;
            boolean validY = eY >= y  && eY <= y + height;
            return validX && validY;
        };
    }
}
