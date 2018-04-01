import org.gnome.gdk.*;                                                                                  
import org.gnome.gtk.*;
import ui.*;
import db.*;

public class Controller {                                                                                
	public static void main(String[] args) {
		Gtk.init(args);                                                                                  
		new ui.Inventory(new db.Database("sample.csv"));                                                                                     
		Gtk.main();                                                                                   
	}                                                                                                    
}	
