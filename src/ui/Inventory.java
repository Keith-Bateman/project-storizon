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

import java.util.*;

import org.gnome.gdk.*;
import org.gnome.gtk.*;

import ctrl.Controller;
import db.*;

public class Inventory extends org.gnome.gtk.Window {
	public Database data;
	private TreeView displayTable;
	private ListStore tablemodel;
	private final Inventory inv = this;
	
	public Inventory(Database db) {
		data = db;
		
		setTitle("Inventory");
		
		initUI();  
		
		connect (new org.gnome.gtk.Window.DeleteEvent() {
			public boolean onDeleteEvent(Widget source, Event event) {
				inv.close();
				return false;
			}	
		});
		                                                                                               
        setDefaultSize(800, 600);  
        setPosition(WindowPosition.CENTER);                                                              
        showAll();
	}
	
	public void refresh() {
		for (Widget w : this.getChildren()) {
			this.remove(w);
		}
		
		initUI();
		
		showAll();
	}
	
	public int getSelectedRow() {
		if (displayTable.getSelection().getSelected() != null) {
			return tablemodel.getPath(displayTable.getSelection().getSelected()).getIndices()[0] + 1;
		}
		else {
			return -1;
		}
	}
	
	public void initTable() {
		/* This is the rather complicated manner in which the table display is initialized */
		displayTable = new TreeView();
		TreeIter row;
		CellRendererText renderer;
		TreeViewColumn column;
		final DataColumnString[] colnames = new DataColumnString[data.datatable.get(0).length];
		
		for (int i = 0; i < colnames.length; i++) {
			colnames[i] = new DataColumnString();
		}
		
		tablemodel = new ListStore(colnames);
		
		for (int r = 0; r < data.datatable.size()-1; r++) {
			row = tablemodel.appendRow();
			for (int c = 0; c < data.datatable.get(r).length; c++) {
				tablemodel.setValue(row, colnames[c], data.datatable.get(r+1)[c]);
			}
		}
		
		displayTable = new TreeView(tablemodel);
		
		for (int i = 0; i < colnames.length; i++) {
			column = displayTable.appendColumn();
			column.setTitle(data.datatable.get(0)[i]);
			renderer = new CellRendererText(column);
			renderer.setText(colnames[i]);
		}
		
		displayTable.connect(new TreeView.RowActivated() {
			public void onRowActivated(TreeView treeview, TreePath treepath, TreeViewColumn treeviewcolumn) {
				TreeIter row;
				String[] info;
				
				row = tablemodel.getIter(treepath);
				
				info = new String[colnames.length];
				
				for (int i = 0; i < colnames.length; i++) {
					info[i] = tablemodel.getValue(row, colnames[i]);
				}
				
				new Edit(data.getHeaders(), info, inv, tablemodel.getPath(row).getIndices()[0] + 1, false);
			}
		});
	}
	
	public void close() {
		this.destroy();
		Controller.close(inv);
	}
	
	public void initUI() {
		VBox vbox = new VBox(false, 0);
		add(vbox);
		
		MenuBar menuBar = new MenuBar();
		ImageMenuItem fileItem = new ImageMenuItem(Stock.FILE);
		MenuItem viewItem = new MenuItem("View");
		MenuItem calcItem = new MenuItem("Calculate");
		
		Menu fileMenu = new Menu();
		ImageMenuItem imnew = new ImageMenuItem(Stock.NEW);
		ImageMenuItem imopen = new ImageMenuItem(Stock.OPEN);
		ImageMenuItem imclose = new ImageMenuItem(Stock.CLOSE);
		ImageMenuItem imsave = new ImageMenuItem(Stock.SAVE);
		ImageMenuItem imsaveas = new ImageMenuItem(Stock.SAVE_AS);
		ImageMenuItem imquit = new ImageMenuItem(Stock.QUIT);
		
		imquit.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				Gtk.mainQuit();
			}
		});

		imnew.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				Controller.open("sample.csv");
			}
		});
		
		imopen.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				FileChooserDialog dlg;
				ResponseType rsp;
				dlg = new FileChooserDialog("Open File", inv, FileChooserAction.OPEN);
				
				rsp = dlg.run();
				dlg.hide();
				
				if (rsp == ResponseType.OK) {
					Controller.open(dlg.getFilename());
				}
			}
		});
		
		imclose.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				close();
			}
		});
		
		imsave.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				data.commit();
			}
		});
		
		imsaveas.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem menuItem) {
				FileChooserDialog dlg;
				ResponseType rsp;
				dlg = new FileChooserDialog("Save As", inv, FileChooserAction.SAVE);
				
				rsp = dlg.run();
				dlg.hide();
				
				if (rsp == ResponseType.OK) {
					data.name = dlg.getFilename();
					data.commit();
				}
			}
		});
		
		fileMenu.append(imnew);
		fileMenu.append(imopen);
		fileMenu.append(imclose);
		fileMenu.append(new SeparatorMenuItem());
		fileMenu.append(imsave);
		fileMenu.append(imsaveas);
		fileMenu.append(new SeparatorMenuItem());
		fileMenu.append(imquit);
		
		fileItem.setSubmenu(fileMenu);
		
		Menu viewMenu = new Menu();
		MenuItem location = new MenuItem("Location");
		CheckMenuItem stock = new CheckMenuItem("Stock");
		CheckMenuItem shipping = new CheckMenuItem("Shipping");
		CheckMenuItem fullscreen = new CheckMenuItem("Fullscreen"); //There is a stock for fullscreen, but doesn't work for checkbox
		
		stock.setActive(true);
		shipping.setActive(false);
		fullscreen.setActive(false);
		
		fullscreen.connect(new CheckMenuItem.Toggled() {
			public void onToggled(CheckMenuItem check) {
				if (check.getActive()) {
					setFullscreen(true);
				}
				else {
					setFullscreen(false);
				}
			}
		});
		
		//viewMenu.append(location);
		//viewMenu.append(new SeparatorMenuItem());
		//viewMenu.append(stock);
		//viewMenu.append(shipping);
		//viewMenu.append(new SeparatorMenuItem());
		viewMenu.append(fullscreen);
		
		viewItem.setSubmenu(viewMenu);
		
		Menu calcMenu = new Menu();
		
		MenuItem amount = new MenuItem("Amount");
		MenuItem costPer = new MenuItem("Cost per");
		MenuItem totalCost = new MenuItem("Total Cost");
		MenuItem beItem = new MenuItem("Breakeven");
		
		Menu beMenu = new Menu();
		
		MenuItem bePrice = new MenuItem("Price");
		MenuItem beDemand = new MenuItem("Demand");
		
		bePrice.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem m) {
				try {
					MessageDialog dlg = new MessageDialog(inv, true, MessageType.QUESTION, ButtonsType.OK,
							"What are the fixed costs?");
					
					Entry e = new Entry();
					
					dlg.add(e);
					dlg.showAll();
					ResponseType choice = dlg.run();
					dlg.hide();
					
					
					data.update(getSelectedRow(), data.queryTable("Price"), "" + Equations.breakevenPrice(Float.parseFloat(e.getText()), Float.parseFloat(data.getCosts()[getSelectedRow()]), Integer.parseInt(data.getDemands()[getSelectedRow()])));
					refresh();
				}
				catch (NumberFormatException e) {
					System.err.println("Fixed costs, cost, or demand not a number");
				}
			}
		});
		
		beDemand.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem m) {
				try {
					MessageDialog dlg = new MessageDialog(inv, true, MessageType.QUESTION, ButtonsType.OK,
							"What are the fixed costs?");
					
					Entry e = new Entry();
					
					dlg.add(e);
					dlg.showAll();
					ResponseType choice = dlg.run();
					dlg.hide();
					
					data.update(getSelectedRow(), data.queryTable("Demand"), "" + Equations.breakevenAmount(Float.parseFloat(e.getText()), Float.parseFloat(data.getCosts()[getSelectedRow()]), Float.parseFloat(data.getPrices()[getSelectedRow()])));
					refresh();
				}
				catch (NumberFormatException e) {
					System.err.println("Fixed costs, price, or cost not a number");
				}
			}
		});
		
		beMenu.append(bePrice);
		beMenu.append(beDemand);
		beItem.setSubmenu(beMenu);
		
		amount.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem m) {
				try {
					data.update(getSelectedRow(), data.queryTable("Amount"), "" + Equations.amount(Float.parseFloat(data.getCosts()[getSelectedRow()]), Float.parseFloat(data.getTotals()[getSelectedRow()])));
					refresh();
				}
				catch (NumberFormatException e) {
					System.err.println("Amount or cost not a number");
				}
			}
		});

		costPer.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem m) {
				try {
					data.update(getSelectedRow(), data.queryTable("Cost per"), "" + Equations.cost(Integer.parseInt(data.getAmounts()[getSelectedRow()]), Float.parseFloat(data.getTotals()[getSelectedRow()])));
					refresh();
				}
				catch (NumberFormatException e) {
					System.err.println("Amount or total cost not a number");
				}
			}
		});
		
		totalCost.connect(new MenuItem.Activate() {
			public void onActivate(MenuItem m) {
				if (getSelectedRow() != -1) {
					try {
						data.update(getSelectedRow(), data.queryTable("Total Cost"), "" + Equations.totalCost(Float.parseFloat(data.getCosts()[getSelectedRow()]), Integer.parseInt(data.getAmounts()[getSelectedRow()])));
						refresh();
					}
					catch (NumberFormatException e) {
						System.err.println("Total cost or cost not a number");
					}
				}
			}
		});
		
		calcMenu.append(amount);
		calcMenu.append(costPer);
		calcMenu.append(totalCost);
		calcMenu.append(beItem);
		
		calcItem.setSubmenu(calcMenu);
		
		menuBar.append(fileItem);
		menuBar.append(viewItem);
		menuBar.append(calcItem);
		
		initTable();
		
		HBox rowManagement = new HBox(false, 0);
		Button plusButton = new Button(Stock.ADD);
		Button deleteButton = new Button(Stock.DELETE);
		
		plusButton.connect(new Button.Clicked() {
			public void onClicked(Button b) {
				new Edit(data.getHeaders(), data.appendRow(), inv, data.dataSize() - 1, true);
			}
		});
		
		deleteButton.connect(new Button.Clicked() {
			public void onClicked(Button b) {
				if (getSelectedRow() != -1) {
					data.deleteRow(getSelectedRow());
				}
				refresh();
			}
		});
		
		rowManagement.add(plusButton);
		rowManagement.add(deleteButton);
		rowManagement.setSizeRequest(1600, 50);

		vbox.packStart(menuBar,  false,  false,  0);
		vbox.add(displayTable);
		vbox.add(rowManagement);
	}
}
