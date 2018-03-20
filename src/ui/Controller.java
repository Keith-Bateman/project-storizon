package ui;

import org.gnome.gdk.*;   
                                                                               
import org.gnome.gtk.*;
	public class Controller {                                                                                
		public static void main(String[] args) {
			Gtk.init(args);                                                                                  
	        new Login();                                                                                     
	        Gtk.main();                                                                                      
	        new Login();                                                                                     
	        Gtk.main();                                                                                      
	    }                                                                                                    
	}
