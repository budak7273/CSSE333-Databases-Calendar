import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class UserAccessControl {
    // TODO: flip this to true to not have to enter login credentials (for testing)
    private static final boolean DEBUG_LOGIN = true;

    private static final Random RANDOM = new SecureRandom();
    private static final Base64.Encoder enc = Base64.getEncoder();
    private static final Base64.Decoder dec = Base64.getDecoder();
    private DatabaseConnectionService dbService;

    String username;
    String password;

    public UserAccessControl(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public String getUsername() {
        return username;
    }

    public void startupPrompt() {
        if (DEBUG_LOGIN) {
            username = "DemoUser";
            password = "DemoPass";
            return;
        }

        // TODO: register OR login question prompt

        int selection = JOptionPane.showOptionDialog(null,
                "Welcome to CalendarDB,\nthe leader in half-baked calendar technology.\nPlease Login or Register.",
                "CalendarDB",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {"Login", "Register"},
                "Register");

        if (selection == 0) {   // Login
            loginPrompt();
        } else {
            registrationPrompt();
        }
    }

    public void loginPrompt() {

        if(DEBUG_LOGIN) {
            username = "DemoUser";
            password = "DemoPass";
        } else {
            username = JOptionPane.showInputDialog("Enter your username.");
            password = JOptionPane.showInputDialog("Enter your password.");
        }

        while (!validateLogin()) {
            JOptionPane.showMessageDialog(null, "Sorry, that username or password is incorrect.\nPlease try again.");

            username = JOptionPane.showInputDialog("Enter your username.");
            password = JOptionPane.showInputDialog("Enter your password.");
        }

    }

    public void registrationPrompt() {
        username = JOptionPane.showInputDialog("Please enter a new username.");
        password = JOptionPane.showInputDialog("Please enter a password.");

        while (!registerUser()) {
            JOptionPane.showMessageDialog(null, "Sorry, that username or password is invalid.\nPlease try a new one.");

            username = JOptionPane.showInputDialog("Please enter a new username.");
            password = JOptionPane.showInputDialog("Please enter a password.");
        }
    }

    public void logout() {
        // TODO
    }

    /**
     * Attempts to log an existing user in using the stored username/password.
     * @return true if the user successfully logged in, false otherwise.
     */
    private boolean validateLogin() {
        CallableStatement saltCS = null;
        CallableStatement verifyHashCS = null;
        try {
            // Get Salt for Username
            dbService.connect();
            saltCS = dbService.getConnection().prepareCall("{? = CALL get_Salt_for_User(?)}");
            saltCS.registerOutParameter(1, Types.INTEGER);
            saltCS.setString(2, username);

            // Execute Query to get Salt
            ResultSet saltRS = saltCS.executeQuery();
            int saltStatus = saltCS.getInt(1);
            saltCS.close();
            
            if (saltStatus != 0) {
                System.out.printf("ERROR: saltStatus = %d", saltStatus);
            }

            // Check if user exists (result set is empty if no user)
            if (!saltRS.next()) {
                System.out.println("ERROR: saltRS is empty");
                return false;
            }

            // Parse Salt from DB
            String saltString = saltRS.getString("Salt");
            if (saltString == null) {
                System.out.println("ERROR: saltRS is null");
                return false;
            }

            System.out.printf("DB returned \"%s\" for saltString of user %s\n", saltString, username);
            byte[] salt = getBytesFromString(saltString);

            // Hash user supplied password with Salt from DB.
            String userHash = hashPassword(salt, password);

            // Send userHash to DB to verify.
            verifyHashCS = dbService.getConnection().prepareCall("{? = CALL verify_Hash_for_User(?, ?)}");
            verifyHashCS.registerOutParameter(1, Types.INTEGER);
            verifyHashCS.setString(2, username);
            verifyHashCS.setString(3, userHash);

            // Execute Query to get Salt
            verifyHashCS.execute();
            int returnedStatus = verifyHashCS.getInt(1);
            verifyHashCS.close();

            // Check status (was password valid?)
            return returnedStatus == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (saltCS != null) {
                    saltCS.close();
                }

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
        byte[] newSalt = getNewSalt();
        System.out.println(Arrays.toString(newSalt));
        String pwHash = hashPassword(newSalt, password);

        System.out.println("gen salt bytes: "+Arrays.toString(newSalt));
        System.out.println("sent salt string: "+getStringFromBytes(newSalt));
        System.out.println("hash string: " + pwHash);

        int status;
        CallableStatement registerCS = null;
        try {
            registerCS = dbService.getConnection().prepareCall("{? = CALL REGISTER(?,?,?)}");
            registerCS.registerOutParameter(1, Types.INTEGER);
            registerCS.setString(2, username);
            registerCS.setString(3, getStringFromBytes(newSalt));
            registerCS.setString(4, pwHash);
            registerCS.execute();
            status = registerCS.getInt(1);
//			System.out.printf("Returned Status = %d\n", status);
            switch (status) {
                case 0:
                    return true;
                case 1:
                    //Username is empty/null.
                    return false;
                case 2:
                    //PWSalt is empty/null.
                    return false;
                case 3:
                    //PasswordHash is empty/null.
                    return false;
                case 4:
                    //Username already exists.
                    return false;
            }
//			return status == 0;
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