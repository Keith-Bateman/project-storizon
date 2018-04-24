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

package ui;

import org.gnome.gtk.*;
import org.gnome.gdk.*;

public class Edit extends org.gnome.gtk.Window {
	private String[] contents;
	private String[] heads;
	public boolean done;
	private Inventory inv;
	private int index;
	private boolean newRow;
	
	public Edit(String[] headers, String[] row, Inventory i, int idx, boolean newP) {
		heads = headers;
		contents = row;
		done = false;
		inv = i;
		index = idx;
		newRow = newP;
		
		setTitle("Edit Row");
		
		initUI();
		
		setDefaultSize(600, 400);  
        setPosition(WindowPosition.CENTER);                                                              
        showAll();
	}
	
	public void close() {
		this.destroy();
	}
	
	public void initUI() {
		VBox vbox = new VBox(false, 300);
		
		add(vbox);
		
		HBox hbox1 = new HBox(false, 0);
		Label[] headers = new Label[heads.length];
		Entry[] inputs = new Entry[contents.length];
		
		HBox hbox2 = new HBox(false, 0);
		Button submit = new Button("Submit");
		Button cancel = new Button("Cancel");
		
		submit.connect(new Button.Clicked() {
			public void onClicked(Button b) {
				inv.data.updateRow(index, contents);
				inv.refresh();
				close();
			}
		});
		
		cancel.connect(new Button.Clicked() {
			public void onClicked(Button b) {
				if (newRow) {
					inv.data.deleteRow(index);
				}
				close();
			}
		});
		
		for (int i = 0; i < contents.length; i++) {
			headers[i] = new Label(heads[i]);
			inputs[i] = new Entry(contents[i]);
			final int x = i;
			inputs[i].connect(new Entry.Changed() {
				public void onChanged(Entry e) {
					contents[x] = e.getText();
				}
			});
			VBox temp = new VBox(false, 0);
			temp.packStart(headers[i], false, false, 0);
			temp.add(inputs[i]);
			hbox1.add(temp);
		}
		hbox1.setSizeRequest(600, 50);
		
		hbox2.add(submit);
		hbox2.add(cancel);
		hbox2.setSizeRequest(600, 50);
		
		vbox.packStart(hbox1, false, false, 0);
		vbox.add(hbox2);
	}
}
