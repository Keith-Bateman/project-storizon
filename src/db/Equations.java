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

public class Equations {
	public static float totalCost(float cost, int amount) {
		return cost * amount;
	}
	
	public static float cost(int amount, float totalCost) {
		return totalCost / amount;
	}
	
	public static int amount(float cost, float totalCost) {
		return (int)(totalCost / cost);
	}
	
	public static float breakevenPrice(float fixCost, float varCost, int demand) {
		return fixCost / demand + varCost;
	}
	
	public static int breakevenAmount(float fixCost, float varCost, float price) {
		return (int)(fixCost / (price - varCost));
	}
}
