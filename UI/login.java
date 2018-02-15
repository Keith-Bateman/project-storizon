package UI;

import org.gnome.gdk.Event;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;
import org.gnome.gtk.WindowPosition;
import org.gnome.gtk.Button;
import org.gnome.gtk.Label;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Fixed;
import org.gnome.gtk.Stock;


public class login extends Window {

	public login() {
		setTitle("Login Screen");

		initUI();
		
		connect((DeleteEvent)(source, event) -> {
				Gtk.mainQuit();
				return false;
			});
		
		setDefaultSize(300, 200);
		setPosition(WindowPosition.CENTER);
		showAll();
	}

	public void initUI() {
		Label loginLabel = new Label("Login: ");
		Entry loginEntry = new Entry();

		// entry.connect((Changed)() -> {
		// 	});

		Button submitButton = new Button("Submit");
		submitButton.setSizeRequest(60, 30);

		submitButton.connect (new Button.Clicked() {
				public void onClicked(Button button) {
					Gtk.mainQuit();
				}
			});
		
		Button closeButton = new Button(Stock.CLOSE);
		
		closeButton.connect(new Button.Clicked() {
				public void onClicked(Button button) {
					Gtk.mainQuit();
				}
			});
		
		Fixed fix = new Fixed();
		fix.put(loginLabel, 10, 20);
		fix.put(loginEntry, 50, 20);
		fix.put(submitButton, 95, 120);
		fix.put(closeButton, 160, 120); //TODO: Align better
		add(fix);
	}
	
	public static void main(String[] args) {
		Gtk.init(args);
		new login();
		Gtk.main();
	}
}
