package ui;

import org.gnome.gdk.*;
import org.gnome.gtk.*;
import db.*;

public class Inventory extends org.gnome.gtk.Window {
	public Database data;
	
	public Inventory(Database db) {
		data = db;
		
		setTitle("Inventory");
		
		initUI();                                                                                        
        
        connect(new org.gnome.gtk.Window.DeleteEvent() {
        	public boolean onDeleteEvent(Widget source, Event event) {                
                Gtk.mainQuit();                                                                          
                return false;
        	}
        });                                                                                          
                                                                                                         
        setDefaultSize(1600, 1200);  
        setPosition(WindowPosition.CENTER);                                                              
        showAll();
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
		
		/* This is the rather complicated way in which the datatable is displayed */
		TreeView displaytable;
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
		
		displaytable = new TreeView(tablemodel);
		
		for (int i = 0; i < colnames.length; i++) {
			column = displaytable.appendColumn();
			column.setTitle(data.datatable.get(0)[i]);
			renderer = new CellRendererText(column);
			renderer.setText(colnames[i]);
		}
		
		displaytable.connect(new TreeView.RowActivated() {
			public void onRowActivated(TreeView treeview, TreePath treepath, TreeViewColumn treeviewcolumn) {
				//Do something?
			}
		});
		
		vbox.packStart(menuBar,  false,  false,  0);
		vbox.add(displaytable);
	}
}
