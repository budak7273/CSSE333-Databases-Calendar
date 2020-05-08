import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {

	private final String DB_USERNAME = "appUserCalendarDB";	// TODO: May not want to store DB access username/password like this
	private final String DB_PASSWORD = "Password123";

	private final String SampleURL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}}";

	private Connection connection = null;

	private String databaseName;
	private String serverName;
	
	private static int connectionsOpen = 0;

	public DatabaseConnectionService(String serverName, String databaseName) {
		this.serverName = serverName;
		this.databaseName = databaseName;
	}

	public boolean connect() {
		String connectionURL = SampleURL.replace(
				"${dbServer}",serverName)
				.replace("${dbName}", databaseName)
				.replace("${user}", DB_USERNAME)
				.replace("${pass}", DB_PASSWORD);
		System.out.print("Attempting to connect to database with username " + DB_USERNAME + "...");
		try {
			connection = DriverManager.getConnection(connectionURL);
			System.out.println("Connection successful.");
			connectionsOpen++;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection failed.");
			return false;
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException ignored) {
		}
		connectionsOpen--;
		System.out.println("Connection closed. " + connectionsOpen + " connections remain open.");
	}
}
