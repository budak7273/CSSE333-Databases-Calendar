import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ClassSectionService {
    private DatabaseConnectionService dbConnectService;
    private String username;

    String getAllClassSectionsQueryString = "{? = call get_All_Class_Sections(?)}";
    String getSubscribedClassSectionsQueryString = "{? = call get_Subscribed_Class_Sections(?)}";
    String getNotSubscribedClassSectionsQueryString = "{? = call get_Not_Subscribed_Class_Sections(?)}";
    String getOwnedClassSectionsQueryString = "{? = call get_Owned_Class_Sections(?)}";



    public ClassSectionService(DatabaseConnectionService dbConnectService, String username) {
        this.dbConnectService = dbConnectService;
        this.username = username;
    }

    /**
     * Creates a new class section based on user prompts.
     * @return true if a new ClassSection was created, false if canceled or failed.
     */
    public boolean newClassSectionPrompt() {
        String courseNumber = JOptionPane.showInputDialog("CourseNumber");
        if (courseNumber == null) return false;

        int sectionNumber;
        String sectionNumberS = JOptionPane.showInputDialog("SectionNumber");
        if (sectionNumberS == null) return false;
        try {
            sectionNumber = Integer.parseInt(sectionNumberS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Invalid Section Number");
            return false;
        }

        int schoolYear;
        String schoolYearS = JOptionPane.showInputDialog("SchoolYear");
        if (schoolYearS == null) return false;
        try {
            schoolYear = Integer.parseInt(schoolYearS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Invalid School Year");
            return false;
        }

        String className = JOptionPane.showInputDialog("ClassName");
        if (className == null) return false;

        int classCalendarID;
        String classCalendarIDS = JOptionPane.showInputDialog("ClassCalendarID");
        if (classCalendarIDS == null) return false;
        try {
            classCalendarID = Integer.parseInt(classCalendarIDS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Invalid ClassCalendarID");
            return false;
        }

        ClassSection newCS = new ClassSection(-1, courseNumber, sectionNumber, className,  schoolYear, username, false);

        return insertClassSection(newCS, classCalendarID);
    }

    /**
     * Deletes a class section based on user prompts.
     * @return true if the ClassSection was deleted, false if canceled or failed.
     */
    public boolean deleteClassSectionPrompt() {
        int classSectionID = classSectionSelectorPrompt(getClassSectionsOwned(), "Select the Class Selection you would like to join", true);
        if (classSectionID == -1) return false;

        return deleteClassSection(classSectionID);
    }

    /**
     * Add user to class section based on user prompts.
     * @return true if user was successfully added, false if canceled or failed.
     */
    public boolean joinClassSectionPrompt() {
//        int classSectionID = classSectionSelectorPrompt(getNotSubscribedClassSections(), "Select the Class Selection you would like to join", true);  //Not Sub SP doesnt work...not very important...and I guess it's a feature since you can now double subscribe??
        int classSectionID = classSectionSelectorPrompt(getAllClassSections(), "Select the Class Selection you would like to join", true);
        if (classSectionID == -1) return false;

        return joinClassSection(classSectionID);
    }

    /**
     * Detaches a User's Class calendar from its class section via prompts.
     * @return true if successfully detached added, false if canceled or failed.
     */
    public boolean leaveClassSectionPrompt() {
        String calendarIDS = JOptionPane.showInputDialog("Enter the CalendarID you would Like to detach from a Class Calendar");
        if (calendarIDS == null) return false;
        int classCalendarID;
        try {
            classCalendarID = Integer.parseInt(calendarIDS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Invalid ClassCalendarID");
            return false;
        }

        return leaveClassSection(classCalendarID);
    }


    public boolean leaveClassSection(int classCalendarIDSubscribedToClassSection) {
        System.out.printf("Leaving Class Section attached to Class Calendar %s... ", classCalendarIDSubscribedToClassSection);


        dbConnectService.connect();
        CallableStatement leave = null;
        int returnedStatus = 5001;
        String queryString = "{? = call leave_Class_Section(?)}";

        try {
            leave = this.dbConnectService.getConnection().prepareCall(queryString);
            leave.registerOutParameter(1, Types.INTEGER);
            leave.setInt(2, classCalendarIDSubscribedToClassSection);


            leave.execute();

            returnedStatus = leave.getInt(1);
            System.out.printf("leave_Class_Section returned status %d... ", returnedStatus);
            if (returnedStatus != 0) return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (leave != null) {
                    System.out.println("Closing leaveClassSection Connection\n");
                    leave.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean insertClassSection(ClassSection newCS, int classCalendarID) {
        System.out.printf("Inserting ClassSection %s...", newCS.toDebugString());

        dbConnectService.connect();
        CallableStatement insertClassSectionPS = null;
        int returnedStatus = 5001;
        String queryString = "{? = call insert_ClassSection(?, ?, ?, ?, ?, ?, ?)}";

        try {
            insertClassSectionPS = this.dbConnectService.getConnection().prepareCall(queryString);
            int i = 1;
            insertClassSectionPS.registerOutParameter(i++, Types.INTEGER);
            insertClassSectionPS.setString(i++, newCS.CourseNumber);
            insertClassSectionPS.setInt(i++, newCS.SectionNumber);
            insertClassSectionPS.setString(i++, newCS.ClassName);
            insertClassSectionPS.setBoolean(i++, false);
            insertClassSectionPS.setInt(i++, newCS.SchoolYear);
            insertClassSectionPS.setString(i++, username);
            insertClassSectionPS.setInt(i++, classCalendarID);

            insertClassSectionPS.execute();

            returnedStatus = insertClassSectionPS.getInt(1);
            System.out.printf("delete_Class_Section returned status %d\n", returnedStatus);
            if (returnedStatus != 0) return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (insertClassSectionPS != null) {
                    System.out.println("Closing deleteClassSection Connection");
                    insertClassSectionPS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean deleteClassSection(int classSectionID) {
        System.out.printf("Deleting Class Section ID %d", classSectionID);

        dbConnectService.connect();
        CallableStatement deleteClassSectionPS = null;
        int returnedStatus = 5001;
        String queryString = "{? = call delete_Class_Section(?)}";

        try {
            deleteClassSectionPS = this.dbConnectService.getConnection().prepareCall(queryString);
            deleteClassSectionPS.registerOutParameter(1, Types.INTEGER);
            deleteClassSectionPS.setInt(2, classSectionID);

            deleteClassSectionPS.execute();

            returnedStatus = deleteClassSectionPS.getInt(1);
            System.out.printf("delete_Class_Section returned status %d", returnedStatus);
            if (returnedStatus != 0) return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (deleteClassSectionPS != null) {
                    System.out.println("Closing deleteClassSection Connection");
                    deleteClassSectionPS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean joinClassSection(int classSectionID) {
        System.out.printf("Joining Class Section ID %d... ", classSectionID);


        dbConnectService.connect();
        CallableStatement joinClassSectionPS = null;
        int returnedStatus = 5001;
        String queryString = "{? = call join_Class_Section(?, ?)}";

        try {
            joinClassSectionPS = this.dbConnectService.getConnection().prepareCall(queryString);
            joinClassSectionPS.registerOutParameter(1, Types.INTEGER);
            joinClassSectionPS.setString(2, username);
            joinClassSectionPS.setInt(3, classSectionID);


            joinClassSectionPS.execute();

            returnedStatus = joinClassSectionPS.getInt(1);
            System.out.printf("join_Class_Section returned status %d\n", returnedStatus);
            if (returnedStatus != 0) return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (joinClassSectionPS != null) {
                    System.out.println("Closing joinClassSection Connection");
                    joinClassSectionPS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Simple prompt to choose a specific class section from a given list of class sections.
     * @param options the ClassSections to choose from
     * @param promptText the text to display on the prompt
     * @param addNoneOption true if a "None" option should be added (will return -1)
     * @return the class section ID that was chosen (or -1 if "None" option is selected or was cancelled)
     */
    public int classSectionSelectorPrompt(ArrayList<ClassSection> options, String promptText, boolean addNoneOption) {
        if (options.size() == 0) return -1;

//        ArrayList<String> optionsStrings = new ArrayList<>();
//        if (addNoneOption) optionsStrings.add("None");
//        options.forEach(cs -> optionsStrings.add(0, cs.toString()));

        ClassSection selection = (ClassSection) JOptionPane.showInputDialog(null, promptText, "Choose a Class Section",
                JOptionPane.PLAIN_MESSAGE,null, options.toArray(new ClassSection[0]), options.get(0));
        if (addNoneOption) {
//            if (selection == 0) return -1;
//            else selection--;
        }

        return selection.ClassSectionID;
    }

    /**
     * Retrieves all class sections from the Database.
     * @return an ArrayList containing the ClassSections or null if failed.
     */
    public ArrayList<ClassSection> getAllClassSections() {
        return executeGetClassSectionQuery(getAllClassSectionsQueryString);
    }

    /**
     * Retrieves all class sections the user is currently subscribed to (they own a calendar that is updated by a class section) from the Database.
     * @return an ArrayList containing the ClassSections or null if failed.
     */
    public ArrayList<ClassSection> getSubscribedClassSections() {
        return executeGetClassSectionQuery(getSubscribedClassSectionsQueryString);
    }

    /**
     * Retrieves all class sections the user is not currently subscribed to (they do not own a calendar that is updated by a class section) from the Database.
     * @return an ArrayList containing the ClassSections or null if failed.
     */
    public ArrayList<ClassSection> getNotSubscribedClassSections() {
        return executeGetClassSectionQuery(getNotSubscribedClassSectionsQueryString);
    }

    /**
     * Retrieves all class sections the user is the owner of (they own the master calendar) from the Database.
     * @return an ArrayList containing the ClassSections or null if failed.
     */
    public ArrayList<ClassSection> getClassSectionsOwned() {
        return executeGetClassSectionQuery(getOwnedClassSectionsQueryString);
    }

    private ArrayList<ClassSection> executeGetClassSectionQuery(String queryString) {
        System.out.printf("Getting ClassSections according to %s...", queryString);

        ArrayList<ClassSection> classSections = new ArrayList<>();

        dbConnectService.connect();
        CallableStatement getClassSectionsPS = null;
        int returnedStatus = 5001;

        try {
            getClassSectionsPS = this.dbConnectService.getConnection().prepareCall(queryString);
            getClassSectionsPS.registerOutParameter(1, Types.INTEGER);
            getClassSectionsPS.setString(2, username);

            ResultSet rs = getClassSectionsPS.executeQuery();

            while(rs.next()) {
                ClassSection newCS = new ClassSection(
                        rs.getInt("ClassSectionID"),
                        rs.getString("CourseNumber"),
                        rs.getInt("SectionNumber"),
                        rs.getString("ClassName"),
                        rs.getInt("SchoolYear"),
                        rs.getString("ParentUserID"),
                        rs.getBoolean("IsPrivate")
                );
                classSections.add(newCS);
            }

            returnedStatus = getClassSectionsPS.getInt(1);
            System.out.printf("get_All_Class_Sections returned status %d and %d results", returnedStatus, classSections.size());
            if (returnedStatus != 0) return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (getClassSectionsPS != null) {
                    System.out.println("Closing getAllClassSections Connection");
                    getClassSectionsPS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return classSections;
    }

    public class ClassSection {
        int ClassSectionID;
        String CourseNumber;
        int SectionNumber;
        String ClassName;
        int SchoolYear;
        String ParentUserID;
        boolean isPrivate;

        public ClassSection(int classSectionID, String courseNumber, int sectionNumber,String className, int schoolYear, String parentUserID, boolean isPrivate) {
            ClassSectionID = classSectionID;
            CourseNumber = courseNumber;
            SectionNumber = sectionNumber;
            ClassName = className;
            SchoolYear = schoolYear;
            ParentUserID = parentUserID;
            this.isPrivate = isPrivate;
        }

        @Override
        public String toString() {
            return String.format("%s-%d (Yr:%d, CB: %s, ID: %d): %s", CourseNumber, SectionNumber, SchoolYear, ParentUserID, ClassSectionID, ClassName);
        }

        public String toDebugString() {
            return "ClassSection{" +
                    "ClassSectionID=" + ClassSectionID +
                    ", CourseNumber='" + CourseNumber + '\'' +
                    ", SectionNumber=" + SectionNumber +
                    ", SchoolYear=" + SchoolYear +
                    ", ParentUserID='" + ParentUserID + '\'' +
                    ", isPrivate=" + isPrivate +
                    '}';
        }
    }
}
