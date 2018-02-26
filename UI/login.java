package UI;

import org.gnome.gdk.Event;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;
import org.gnome.gtk.WindowPosition;
import org.gnome.gtk.Button;
import org.gnome.gtk.Label;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Stock;
import org.gnome.gtk.Alignment;
import org.gnome.gtk.HBox;
import org.gnome.gtk.VBox;

public class login extends Window {

	private String username;
	private String password;
	
	public login() {
		setTitle("Login Screen");

		initUI();
		
		connect((DeleteEvent)(source, event) -> {
				Gtk.mainQuit();
				return false;
			});
		
		setDefaultSize(600, 400);
		setPosition(WindowPosition.CENTER);
		showAll();
	}

	public void initUI() {
		Label unLabel = new Label("Username: ");
		Label pwLabel = new Label("Password: ");
		Entry unEntry = new Entry();
		Entry pwEntry = new Entry();

		pwEntry.setVisibility(false);

		Button submitButton = new Button("Submit");
		submitButton.setSizeRequest(60, 30);

		submitButton.connect (new Button.Clicked() {
				public void onClicked(Button button) {
					username = unEntry.getText();
					password = pwEntry.getText();
					System.out.println("UN: " + username + "\nPW: " + password);
					Gtk.mainQuit();
				}
			});
		
		Button closeButton = new Button(Stock.CLOSE);
		
		closeButton.connect(new Button.Clicked() {
				public void onClicked(Button button) {
					Gtk.mainQuit();
				}
			});

		VBox vbox = new VBox(false, 5);
		HBox hbox = new HBox(true, 3);

		hbox.add(submitButton);
		hbox.add(closeButton);
		
		Alignment align = new Alignment(1, 0, 0, 0);
		align.add(hbox);

		vbox.packStart(unLabel, false, false, 3);
		vbox.add(unEntry);
		vbox.add(pwLabel);
		vbox.add(pwEntry);
		vbox.add(align);
		
		add(vbox);
	}
}
