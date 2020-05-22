import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class UserAccessControl {
    // TODO: flip this to true to not have to enter login credentials (for testing. uses the DemoUser/DemoPass account)
    private static final boolean SKIP_LOGIN = false;
    // TODO: flip this to true to always have your passwords be correct (for resetting a lost password)
    private static final boolean UAC_GOD_MODE = false;

    private static final Random RANDOM = new SecureRandom();
    private static final Base64.Encoder enc = Base64.getEncoder();
    private static final Base64.Decoder dec = Base64.getDecoder();
    private DatabaseConnectionService dbService;

    String username;
    String password;
    String userRealName;

    public UserAccessControl(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Allows user to login or register.
     * @return true if logged in (or registered (and was logged in automatically)) successfully, or false if the
     * register/login prompt's cancel button was pressed.
     */
    public boolean startupPrompt() {
        if (SKIP_LOGIN) {
            username = "DemoUser";
            password = "DemoPass";
            return true;
        }

        int selection = JOptionPane.showOptionDialog(null,
                "Welcome to CalendarDB,\nthe leader in half-baked calendar technology.\nPlease Login or Register.",
                "CalendarDB",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {"Login", "Register", "Close"},
                "Register");



        if (selection == 2) {   // Close Button
            System.exit(0); // User cancelled login/register screen, so exit application
            return false;
        } else if (selection == 0) {   // Login
            if(!loginPrompt()) {        // Cancelled login, so show startup.
                return startupPrompt();
            }
            return true;
        } else {    // Register
            if(!registrationPrompt()) { // Cancelled register, so show startup.
                return startupPrompt();
            }
            return true;
        }
    }

    /**
     * Logs in the user via displayed prompts.
     * @return true if login successful, false if a cancel button was pressed.
     */
    public boolean loginPrompt() {

        if(SKIP_LOGIN) {
            username = "DemoUser";
            password = "DemoPass";
        } else {
            username = JOptionPane.showInputDialog("Enter your username.");
            if (username == null) return false; // User Cancelled

            password = simplePasswordPrompt("Enter your password.");
            if (password == null) return false;
        }

        while (!validatePassword()) {
            JOptionPane.showMessageDialog(null, "Sorry, that username or password is incorrect.\nPlease try again.");

            username = JOptionPane.showInputDialog("Enter your username.");
            if (username == null) return false;

            password = simplePasswordPrompt("Enter your password.");
            if (password == null) return false;
        }
        return true; // Valid Login
    }

    /**
     * Registers a new user via displayed prompts.
     * @return true if registration successful, false if a cancel button was pressed.
     */
    public boolean registrationPrompt() {
        username = JOptionPane.showInputDialog("Please enter a new username.");
        if (username == null) return false;

        password = doublePasswordPrompt("Please enter a password for your new account.");
        if (password == null) return false;

        userRealName = JOptionPane.showInputDialog("Please enter your real name.");
        if (userRealName == null) return false;  // User Cancelled.

        while (!registerUser()) {
            JOptionPane.showMessageDialog(null, "Sorry, that username or password is invalid.\nPlease try a new one.");

            username = JOptionPane.showInputDialog("Please enter a new username.");
            if (username == null) return false;
            password = doublePasswordPrompt("Please enter a password for your new account.");
            if (password == null) return false;
        }
        return true;    // Registration Successful
    }

    /**
     * Resets a user's password via displayed prompts.
     * @return true if reset successful, false if a cancel button was pressed.
     */
    public boolean resetPasswordPrompt() {
        password = simplePasswordPrompt("Please enter your current password.");
        if (password == null) return false;  // User cancelled

        if (validatePassword()) {
            String newPassword = doublePasswordPrompt("Please enter a new password.");
            if (newPassword == null) {  // User cancelled
                JOptionPane.showMessageDialog(null, "Password Change Failed. Please try again later.");
                return false;
            }

            if (resetPassword(newPassword)) {
                JOptionPane.showMessageDialog(null, "Success! Your password was successfully changed.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Password Change Failed. Please try again later.");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, that password is incorrect.");
            return false;
        }
    }

    /**
     * Prompts the user to enter a new password twice.
     * @param promptMessage the message to display on the prompt.
     * @return entered string, or null if the user selected cancel on any prompt.
     */
    private String doublePasswordPrompt(String promptMessage) {
        String pw1, pw2;
        do {
            pw1 = simplePasswordPrompt(promptMessage);
            if (pw1 == null) return null;
            pw2 = simplePasswordPrompt(promptMessage + " (Again)");
            if (pw2 == null) return null;

            if (pw1.equals(pw2)) {
                return pw1;
            } else {
                JOptionPane.showMessageDialog(null, "Passwords did not match. Please try again.");
            }

        } while (true);
    }


    /**
     * Simple password prompt with obscured characters.
     * Based on https://stackoverflow.com/questions/9924289/hide-data-using-joptionpane
     * @param promptMessage message to display on the prompt window.
     * @return the password, or null if cancel button is pressed.
     */
    private String simplePasswordPrompt(String promptMessage) {
            JPasswordField jpf = new JPasswordField(30);
            JLabel jl = new JLabel(promptMessage + "\n");
            Box box = Box.createHorizontalBox();
            box.add(jl);
            box.add(jpf);
            int x = JOptionPane.showConfirmDialog(null, box, "CalendarDB", JOptionPane.OK_CANCEL_OPTION);

            if (x == JOptionPane.OK_OPTION) {
                System.out.println(jpf.getText());
                return jpf.getText();
            }
            return null;
    }



    /**
     * Resets the current user's password if the currently stored password is correct.
     * @return true if successful, false otherwise.
     */
    private boolean resetPassword(String newPassword) {
        byte[] salt = getStoredSalt();
        validatePassword(salt);
        
        // Hash user supplied password with Salt from DB.
        if (salt == null) {
            System.out.println("ERROR: salt is null");
            return false;
        }
        String oldHash = hashPassword(salt, password);
        String newHash = hashPassword(salt, newPassword);

        if (UAC_GOD_MODE) {
            oldHash = JOptionPane.showInputDialog("GOD MODE: Please enter Old Hash");
        }

        CallableStatement updatePasswordCS = null;
        try {
            // Build Password Change Request
            System.out.printf("Password (Hash) Change Request for User \"%s\" from \"%s\" to \"%s\"...", username, oldHash, newHash);
            dbService.connect();
            updatePasswordCS = dbService.getConnection().prepareCall("{? = CALL update_User_Password(?, ?, ?)}");
            updatePasswordCS.registerOutParameter(1, Types.INTEGER);
            updatePasswordCS.setString(2, username);
            updatePasswordCS.setString(3, oldHash);
            updatePasswordCS.setString(4, newHash);

            // Execute Password Change Request
            updatePasswordCS.execute();
            int pwChangeStatus = updatePasswordCS.getInt(1);
            updatePasswordCS.close();

            // Check return status (was current password valid?)
            if (pwChangeStatus != 0) {
                System.out.printf("ERROR: pwChangeStatus = %d\n", pwChangeStatus);
            }
            return pwChangeStatus == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (updatePasswordCS != null) {
                    updatePasswordCS.close();
                }
            } catch (SQLException ignored) {}
        }
        return false;
    }

    private boolean validatePassword() {
        byte[] salt = getStoredSalt();
        return validatePassword(salt);
    }

    /**
     * Attempts to log an existing user in using the stored username/password.
     * @return true if the user successfully logged in, false otherwise.
     */
    private boolean validatePassword(byte[] salt) {
        if (UAC_GOD_MODE) return true;
        // Hash user supplied password with Salt from DB.
        if (salt == null) {
            System.out.println("ERROR: salt is null");
            return false;
        }
        String userHash = hashPassword(salt, password);
//        System.out.printf("userhash = %s\n", userHash);

        CallableStatement verifyHashCS = null;
        try {
            // Send userHash to DB to verify.
            System.out.printf("Validating Password Hash for user \"%s\", hash \"%s\"...", username, userHash);
            dbService.connect();
            verifyHashCS = dbService.getConnection().prepareCall("{? = CALL verify_Hash_for_User(?, ?)}");
            verifyHashCS.registerOutParameter(1, Types.INTEGER);
            verifyHashCS.setString(2, username);
            verifyHashCS.setString(3, userHash);

            // Execute Query to get Salt
            verifyHashCS.execute();
            int hashStatus = verifyHashCS.getInt(1);
            verifyHashCS.close();

            // Check status (was password valid?)
            if (hashStatus != 0) {
                System.out.printf("ERROR: hashStatus = %d\n", hashStatus);
            }
            return hashStatus == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (verifyHashCS != null) {
                    verifyHashCS.close();
                }
            } catch (SQLException ignored) {}
        }
        return false;
    }

    /**
     * Attempts to register a new user with stored username/password.
     * @return true if the user was successfully registered, false otherwise.
     */
    private boolean registerUser() {
        // Generate new salt
        byte[] newSalt = getNewSalt();
        String newSaltString = getStringFromBytes(newSalt);

        // Hash new password
        String newHash = hashPassword(newSalt, password);

//        System.out.println("gen salt bytes: "+Arrays.toString(newSalt));
//        System.out.println("sent salt string: "+getStringFromBytes(newSalt));
//        System.out.println("hash string: " + userHash);

        CallableStatement registerCS = null;
        try {
            // Call register_User SP
            System.out.printf("Registering New User \"%s\", with username \"%s\" with salt \"%s\", hash \"%s\"...", userRealName, username, newSaltString, newHash);
            dbService.connect();
            registerCS = dbService.getConnection().prepareCall("{? = CALL register_User(?, ?, ?, ?)}");
            registerCS.registerOutParameter(1, Types.INTEGER);
            registerCS.setString(2, username);
            registerCS.setString(3, userRealName);
            registerCS.setString(4, newSaltString);
            registerCS.setString(5, newHash);

            // Execute register_user Query
            registerCS.execute();
            int returnedStatus = registerCS.getInt(1);
            registerCS.close();

            // Check status
            return returnedStatus == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (registerCS != null) {
                    registerCS.close();
                }
            } catch (SQLException ignored) {}
        }
        return false;
    }

    /**
     * Gets the salt currently stored in the database for the current user.
     * @return the stored salt, or null if error (user probably doesnt exist).
     */
    public byte[] getStoredSalt() {
        byte[] salt = null;
        CallableStatement saltCS = null;
        try {
            // Get Salt for Username
            System.out.printf("Getting Salt for user %s...", username);
            dbService.connect();
            saltCS = dbService.getConnection().prepareCall("{CALL get_Salt_for_User(?)}");
            saltCS.setString(1, username);

            // Execute Query to get Salt
            ResultSet saltRS = saltCS.executeQuery();

            // Check if user exists (result set is empty if no user)
            if (saltRS.isClosed()) {
                System.out.println("ERROR: saltRS is closed.");
                return null;
            }
            if (!saltRS.next()) {
                System.out.println("ERROR: saltRS is empty");
                return null;
            }

            // Parse Salt from DB
            String saltString = saltRS.getString("Salt");
            saltCS.close();

            if (saltString == null) {
                System.out.println("ERROR: saltRS is null");
                return null;
            }

            System.out.printf("\tDB returned \"%s\" for saltString of user %s\n", saltString, username);
            salt = getBytesFromString(saltString);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (saltCS != null) {
                    saltCS.close();
                }
            } catch (SQLException ignored) {}
        }
        return salt;
    }

    /**
     * Securely generates a new password salt.
     * @return the new password salt
     */
    public byte[] getNewSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public String hashPassword(byte[] salt, String password) {

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f;
        byte[] hash = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
            e.printStackTrace();
        }
        return getStringFromBytes(hash);
    }

    public String getStringFromBytes(byte[] data) {
        return enc.encodeToString(data);
    }
    
    public byte[] getBytesFromString(String data) {
        return dec.decode(data);
    }
}