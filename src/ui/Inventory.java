package ui;

import javax.swing.JFileChooser;
import java.util.*;

import org.gnome.gdk.*;
import org.gnome.gtk.*;

import ctrl.Controller;
import db.*;

public class Inventory extends org.gnome.gtk.Window {
	public Database data;
	private TreeView displayTable;
	
	public Inventory(Database db) {
		data = db;
		
		setTitle("Inventory");
		
		initUI();                                                                                        
        /*
        connect(new org.gnome.gtk.Window.DeleteEvent() {
        	public boolean onDeleteEvent(Widget source, Event event) {                
                Gtk.mainQuit();                                                                          
                return false;
        	}
        });                                                                                          
        */                                                                                               
        setDefaultSize(1600, 1200);  
        setPosition(WindowPosition.CENTER);                                                              
        showAll();
	}
	
	public void initTable() {
		/* This is the rather complicated manner in which the table display is initialized */
		displayTable = new TreeView();
		ListStore tablemodel;
		TreeIter row;
		CellRendererText renderer;
		TreeViewColumn column;
		DataColumnString[] colnames = new DataColumnString[data.datatable.get(0).length];
		
		//Statusbar statusbar = new Statusbar();
		
		for (int i = 0; i < colnames.length; i++) {
			colnames[i] = new DataColumnString();
		}
		
		tablemodel = new ListStore(colnames);
		
		for (int r = 0; r < data.datatable.size()-1; r++) {
			row = tablemodel.appendRow();
			for (int c = 0; c < data.datatable.get(r).length; c++) {
				tablemodel.setValue(row, colnames[c], data.datatable.get(r+1)[c]);
			}
		}
		
		displayTable = new TreeView(tablemodel);
		
		for (int i = 0; i < colnames.length; i++) {
			column = displayTable.appendColumn();
			column.setTitle(data.datatable.get(0)[i]);
			renderer = new CellRendererText(column);
			renderer.setText(colnames[i]);
		}
		
		displayTable.connect(new TreeView.RowActivated() {
			public void onRowActivated(TreeView treeview, TreePath treepath, TreeViewColumn treeviewcolumn) {
				//Do something?
			}
		});
	}
	
	public void close() {
		this.destroy();
	}
	
	public void initUI() {
		VBox vbox = new VBox(false, 0);
		add(vbox);
		
		MenuBar menuBar = new MenuBar();
		ImageMenuItem fileItem = new ImageMenuItem(Stock.FILE);
		MenuItem viewItem = new MenuItem("View");
		MenuItem forecastItem = new MenuItem("Forecast");
		
		Menu fileMenu = new Menu();
		ImageMenuItem imnew = new ImageMenuItem(Stock.NEW);
		ImageMenuItem imopen = new ImageMenuItem(Stock.OPEN);
		ImageMenuItem imclose = new ImageMenuItem(Stock.CLOSE);
		ImageMenuItem imsave = new ImageMenuItem(Stock.SAVE);
		ImageMenuItem imsaveas = new ImageMenuItem(Stock.SAVE_AS);
		ImageMenuItem imquit = new ImageMenuItem(Stock.QUIT);
		
		imquit.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				Gtk.mainQuit();
			}
		});

		imnew.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				Controller.open("sample.csv");
			}
		});
		
		imopen.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				try {
					Controller.open(fc.getSelectedFile().getPath());
					close();
				} catch (Exception e) {
					System.err.println("Error while choosing file: " + e.getMessage());
				}
			}
		});
		
		imclose.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				close();
			}
		});
		
		imsave.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				data.commit();
			}
		});
		
		imsaveas.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				try {
					data.name = fc.getSelectedFile().getPath();
					data.commit();
				} catch (Exception e) {
					System.err.println("Error while choosing file: " + e.getMessage());
				}
			}
		});
		
		fileMenu.append(imnew);
		fileMenu.append(imopen);
		fileMenu.append(imclose);
		fileMenu.append(new SeparatorMenuItem());
		fileMenu.append(imsave);
		fileMenu.append(imsaveas);
		fileMenu.append(new SeparatorMenuItem());
		fileMenu.append(imquit);
		
		fileItem.setSubmenu(fileMenu);
		
		Menu viewMenu = new Menu();
		MenuItem location = new MenuItem("Location");
		CheckMenuItem stock = new CheckMenuItem("Stock");
		CheckMenuItem shipping = new CheckMenuItem("Shipping");
		CheckMenuItem fullscreen = new CheckMenuItem("Fullscreen"); //There is a stock for fullscreen, but doesn't work for checkbox
		
		stock.setActive(true);
		shipping.setActive(false);
		fullscreen.setActive(false);
		
		fullscreen.connect(new CheckMenuItem.Toggled() {
			public void onToggled(CheckMenuItem check) {
				if (check.getActive()) {
					setFullscreen(true);
				}
				else {
					setFullscreen(false);
				}
			}
		});
		
		viewMenu.append(location);
		viewMenu.append(new SeparatorMenuItem());
		viewMenu.append(stock);
		viewMenu.append(shipping);
		viewMenu.append(new SeparatorMenuItem());
		viewMenu.append(fullscreen);
		
		viewItem.setSubmenu(viewMenu);
		
		menuBar.append(fileItem);
		menuBar.append(viewItem);
		menuBar.append(forecastItem);
		
		initTable();
		
		vbox.packStart(menuBar,  false,  false,  0);
		vbox.add(displayTable);
	}
}
