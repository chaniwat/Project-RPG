package com.skyhouse.projectrpg.data;

import java.util.ArrayList;

/**
 * Inventory data.
 * @author Meranote
 */
public class InventoryData {
	
	public int money;
	public ArrayList<Integer> inventory;
	
	/**
	 * Construct a new InventoryData.
	 */
	public InventoryData() {
		inventory = new ArrayList<Integer>();
	}

}
