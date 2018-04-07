package ctrl;

import org.gnome.gdk.*;                                                                                  
import org.gnome.gtk.*;
import ui.*;
import db.*;

public class Controller {                                                                                
	public static void main(String[] args) {
		Gtk.init(args);  
		open("sample.csv");
	    Gtk.main();                                                                                   
	}
	
	public static void open(String fileName) {
		new ui.Inventory(new db.Database(fileName));
	}
}
