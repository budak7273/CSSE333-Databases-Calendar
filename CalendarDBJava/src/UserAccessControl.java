import javax.swing.*;

public class UserAccessControl {
    // TODO: flip this to true to not have to enter login credentials (for testing)
    private static final boolean DEBUG_LOGIN = true;

    String username;
    String password;

    public UserAccessControl() {

    }

    public String getUsername() {
        return username;
    }

    public void startupPrompt() {
        // TODO: register OR login question prompt

        loginPrompt();



    }

    public void loginPrompt() {
        if(DEBUG_LOGIN) {
            username = "DemoUser";
            password = "DemoPass";
        } else {
            username = JOptionPane.showInputDialog("Enter your username.");
            password = JOptionPane.showInputDialog("Enter your password.");
        }
    }

    public void registrationPrompt() {
        // TODO: complete.
    }

    public void logout() {
        // TODO
    }

}
