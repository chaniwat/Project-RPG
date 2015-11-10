package com.skyhouse.projectrpg.data;

import java.util.HashMap;

/**
 * Inventory data.
 * @author Meranote
 */
public class InventoryData {
	
	public int money;
	/**
	 * Key is <b>position</b>, value is <b>item id</b>.
	 */
	public HashMap<Integer, Integer> itemPosition;
	/**
	 * Key is <b>position</b>, value is <b>quantity</b>.
	 */
	public HashMap<Integer, Integer> itemQuantity;
	
	/**
	 * Construct a new InventoryData.
	 */
	public InventoryData() {
		itemPosition = new HashMap<Integer, Integer>();
		itemQuantity = new HashMap<Integer, Integer>();
	}

}
