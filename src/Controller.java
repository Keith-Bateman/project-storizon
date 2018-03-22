import org.gnome.gdk.*;                                                                                  
import org.gnome.gtk.*;
import ui.*;

public class Controller {                                                                                
	public static void main(String[] args) {
		Gtk.init(args);                                                                                  
		new ui.Login();                                                                                     
		Gtk.main();                                                                                   
	}                                                                                                    
}	
