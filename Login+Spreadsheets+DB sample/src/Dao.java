import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public static Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	public void createTables() {
		// variables for SQL Query table creations
		final String createUsersTable = "CREATE TABLE mkotly_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30))";
		final String createTicketsTable = "CREATE TABLE Mkotly_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_issue VARCHAR(30), ticket_description VARCHAR(200))";
		final String createArchivesTable = "CREATE TABLE Mkotly_archives(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_issue VARCHAR(30), ticket_description VARCHAR(200))";

		try {

			// create table

			statement = getConnection().createStatement();
			statement.executeUpdate(createArchivesTable);
			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");
			
			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;
		Connection connect = null;
		Statement statement = null;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // array list to hold
														// spreadsheet rows &
														// columns

		// read data from file
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
			statement = connect.createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into mkotly_users(uname,upass) " + "values('" + rowData.get(0) + "','" + rowData.get(1)
						+ "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	// add other desired CRUD methods needed like for updates, deletes, etc.

}
