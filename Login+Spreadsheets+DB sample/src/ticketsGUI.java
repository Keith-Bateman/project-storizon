import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mysql.jdbc.PreparedStatement;

public class ticketsGUI implements ActionListener {

	// class level member objects

	Dao dao = new Dao(); // for CRUD operations
	String chkIfAdmin = null;
	private JFrame mainFrame;

	JScrollPane sp = null;
	JScrollPane asp = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");
	
	/* add any more Main menu object items below */

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuArchives;

	/* add any more Sub object items below */

	// constructor
	public ticketsGUI(String verifyRole) {

		chkIfAdmin = verifyRole;
		JOptionPane.showMessageDialog(null, "Welcome " + verifyRole);
		if (chkIfAdmin.equals("admin"))

			dao.createTables(); // fire up table creations (tickets / user tables)
		/*
		 * else do something else if you like
		 *
		 */

		createMenu();
		prepareGUI();
	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);
		
		// initialize third sub menu items for Admin main menu
		mnuArchives = new JMenuItem("Archives");
		// add to Admin main menu item
		mnuTickets.add(mnuArchives);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuArchives.addActionListener(this);

		// add any more listeners for any additional sub menu items if desired

	}

	private void prepareGUI() {
		// initialize frame object
		mainFrame = new JFrame("Tickets");

		// create jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		
		// add menu bar components to frame
		mainFrame.setJMenuBar(bar);
		mainFrame.addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		mainFrame.setSize(400, 400);
		mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	/*
	 * action listener fires up items clicked on from sub menus with one action
	 * performed event handler!
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			try {

				// get ticket information
				String UserName = chkIfAdmin;
				String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

				// insert ticket information to database
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();
 
				int result = statement 
						.executeUpdate("Insert into mkotly_tickets(ticket_issuer, ticket_issue, ticket_description) values(" + " '"
							+ UserName +"','"	+ ticketName + "','" + ticketDesc + "')", Statement.RETURN_GENERATED_KEYS);

				// retrieve ticket id number newly auto generated upon record
				// insertion
				ResultSet resultSet = null;
				resultSet = statement.getGeneratedKeys();
				int id = 0;
				if (resultSet.next()) {
					id = resultSet.getInt(1); // retrieve first field in table
				}
				// display results if successful or not to console / dialog box
				if (result != 0) {
					System.out.println("Ticket ID : " + id + " created successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
				} else {
					System.out.println("Ticket cannot be created!!!");
				}

			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		} 
		
		
		else if (e.getSource() == mnuItemViewTicket) {

			// retrieve ticket information for viewing in JTable

			try {
				String UserName = chkIfAdmin;
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");
				ResultSet results;
				Statement statement = dbConn.createStatement();
				 if (chkIfAdmin.equals("admin")) {
           results = statement.executeQuery("SELECT * FROM mkotly_tickets");
       } //end of if statement
       else {
           results = statement.executeQuery("SELECT * FROM mkotly_tickets WHERE ticket_issuer LIKE '" + UserName + "'");
       }

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(results));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				mainFrame.add(sp);
				mainFrame.setVisible(true); // refreshes or repaints frame on

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//Update Files
		else if (e.getSource() == mnuItemUpdate)
		{

			try {  
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");
       Statement statement = dbConn.createStatement();


       statement.executeQuery("SELECT * FROM mkoty_tickets");
       String id = JOptionPane.showInputDialog(null, "What Ticket Number?");
       String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
       // screen
       statement.executeUpdate("UPDATE Mkotly_tickets SET ticket_description = " + " '" + ticketDesc + " '" + " WHERE ticket_id = " + id);
        } 

   catch (SQLException e1) 
   {
       e1.printStackTrace();
   }
	}
		//Deletes Files
		else if (e.getSource() == mnuItemDelete)
		{
			try {  
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");
       Statement statement = dbConn.createStatement();

       String id = JOptionPane.showInputDialog(null, "What Ticket is Resolved?");
       int selectedOption = JOptionPane.showConfirmDialog(null, 
           "Do you Want to Delete Ticket" + id, 
           id +"?", 
           JOptionPane.YES_NO_OPTION); 
if (selectedOption == JOptionPane.YES_OPTION) {
       statement.executeUpdate("INSERT INTO Mkotly_archives select * FROM Mkotly_tickets WHERE ticket_id = " + id);
       statement.executeUpdate("DELETE FROM Mkotly_tickets WHERE ticket_id = " + id );
        } 
			}
   catch (SQLException e1) 
   {
       e1.printStackTrace();
   }
		}

		
		//Displays Archives of files
		else if (e.getSource() == mnuArchives) {
			JOptionPane.showMessageDialog(null, "Accessing Archives");

			
			try {
				String UserName = chkIfAdmin;

				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");
				ResultSet rs;
				Statement statement = dbConn.createStatement();
				 if (chkIfAdmin.equals("admin")) {
						new ticketsGUI("admin") ;
           rs = statement.executeQuery("SELECT * FROM Mkotly_archives");
       } //end of if statement
       else {
					new ticketsGUI(UserName) ;

           rs = statement.executeQuery("SELECT * FROM Mkotly_archives WHERE ticket_issuer LIKE '" + UserName + "'");
       }


				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jta = new JTable(ticketsJTable.buildTableModel(rs));

				jta.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jta);
				mainFrame.add(sp);
				mainFrame.setVisible(true); // refreshes or repaints frame on
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
    	}
		
	}
	

