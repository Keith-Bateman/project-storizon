package UI;

import org.gnome.gdk.*;
import org.gnome.gtk.*;

public class controller {
	public static void main(String[] args) {
		Gtk.init(args);
		new login();
		Gtk.main();
		new login();
		Gtk.main();
	}
}
