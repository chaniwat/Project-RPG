package com.skyhouse.projectrpg.tool.itemeditor;

import com.skyhouse.projectrpg.data.item.BaseItem.ItemType;

public class ItemData implements Comparable<ItemData> {

	public int itemid;
	public ItemType type;
	public String name;
	public int maxstack;
	public int requirelevel;
	public String description;
	public String pathtoicon, pathtotexture;
	
	public int heal = 0;
	public int damage = 0;
	public int defense = 0;
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(ItemData o) {
		return name.compareTo(o.name);
	}
	
}
