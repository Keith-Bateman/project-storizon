package ui;

import org.gnome.gdk.*;
import org.gnome.gtk.*;

public class Login extends org.gnome.gtk.Window {                                                   
        
	private Entry unEntry;
	private Entry pwEntry;
	
    public Login() {                                                                                     
        setTitle("Login Screen");                                                                        
                                                                                                         
        initUI();                                                                                        
                                                                                                         
        connect(new org.gnome.gtk.Window.DeleteEvent() {
        	public boolean onDeleteEvent(Widget source, Event event) {                
                Gtk.mainQuit();                                                                          
                return false;
        	}
        });                                                                                          
                                                                                                         
        setDefaultSize(600, 400);                                                                        
        setPosition(WindowPosition.CENTER);                                                              
        showAll();                                                                                       
    }
	
	public void initUI() {                                                                               
        Label unLabel = new Label("Username: ");                                                         
        Label pwLabel = new Label("Password: ");                                                         
        unEntry = new Entry();                                                                     
        pwEntry = new Entry();                                                                     
                                                                                                         
        pwEntry.setVisibility(false);                                                                    
                                                                                                         
        Button submitButton = new Button("Submit");                                                      
        submitButton.setSizeRequest(60, 30);                                                             
        
        submitButton.connect (new Button.Clicked() {                                                     
                public void onClicked(Button button) {
                    System.out.println("UN: " + unEntry.getText() + "\nPW: " + pwEntry.getText());                         
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
