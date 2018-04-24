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

package db;

import java.util.*;
import java.io.*;

public class Database {
	public ArrayList<String[]> datatable;
	public String name;
	
	/* Initialize a database for given filename */
	public Database(String filename) {
		name = filename;
		datatable = new ArrayList<String[]>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String line;
			String[] lineCells;
			while ((line = in.readLine()) != null) {
				lineCells = line.split(",");
				datatable.add(lineCells);
			}
			in.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File " + filename + " not found");
		}
		catch (IOException e) {
			System.out.println("Error: IO Error while trying to read file " + filename);
		}
	}
	
	/* Returns -1 for not found and index if found */
	private int queryTable(String query) {
		String[] headers = datatable.get(0);
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].equals(query)) {
				return i;
			}
		}
		return -1;
	}
	
	private String[] getCol(String colName) {
		int colIndex;
		String[] column = new String[datatable.size()];
		if ((colIndex = queryTable(colName)) == -1) {
			return null;
		}
		for (int i = 0; i < datatable.size(); i++) {
			column[i] = datatable.get(i)[colIndex];
		}
		return column;
	}
	
	public String[] getHeaders() {
		return datatable.get(0);
	}
	
	public String[] getLocations() {
		return getCol("Location");
	}
	
	/* Enter a change in the database */
	public void update(int r, int c, String newval) {
		datatable.get(r)[c] = newval;
	}
	
	/* Change a full row of the database */
	public void updateRow(int r, String[] newvals) {
		datatable.set(r, newvals);
	}
	
	/* Add a new row to the database */
	public String[] appendRow() {
		String[] temp = new String[rowSize()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = "";
		}
		datatable.add(temp);
		return datatable.get(dataSize() - 1);
	}
	
	public void deleteRow(int index) {
		datatable.remove(index);
	}
	
	/* Number of rows */
	public int dataSize() {
		return datatable.size();
	}
	
	/* Number of columns */
	public int rowSize() {
		return datatable.get(0).length;
	}
	
	/* Commit changes to file. Return false if commit fails, true if it succeeds */
	public boolean commit() {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(name, false));
			for (int i = 0; i < datatable.size(); i++) {
				out.println(String.join(",", datatable.get(i)));
			}
			out.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("Error: File " + name + " not found");
			return false;
		}
		catch (IOException e) {
			System.err.println("Error: IO Error while trying to write to file " + name);
			return false;
		}
		return true;
	}
}
