package ui;

import org.gnome.gdk.*;
import org.gnome.gtk.*;

public class Inventory extends org.gnome.gtk.Window {
	
	public Inventory() {
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
		vbox.packStart(menuBar,  false,  false,  3);
	}
}
