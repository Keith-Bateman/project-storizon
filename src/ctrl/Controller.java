/*
Storizon
A free inventory management application

Copyright (C) 2018 Keith Bateman, Michael Kotlyar

Email: 
kbateman@hawk.iit.edu
mkotlyar@hawk.iit.edu

This file is part of Storizon

Storizon is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at
your option) any later version

Storizon is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with Storizon. If not, see http://www.gnu.org/licenses/.
*/

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
	    System.exit(0);
	}
	
	public static void open(String fileName) {
		new ui.Inventory(new db.Database(fileName));
	}
}
