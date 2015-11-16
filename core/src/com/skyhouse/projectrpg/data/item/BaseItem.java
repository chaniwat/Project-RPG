package com.skyhouse.projectrpg.data.item;

public class BaseItem {
	
	public static enum ItemBaseType {
		POTION,
		QUEST,
		WEAPON,
		OFFHAND,
		HELMET,
		ARMOR,
		PANT,
		GLOVE,
		BOOT,
		NECKLACE,
		EARRING,
		RING;
	}

	public int maxstack, requirelevel, requirejob;
	public ItemBaseType type;
	public String name, description, pathtoicon, pathtotexture;
	public int str, agi, intel, damage, defense, heal;
	
	public BaseItem(String name, ItemBaseType type) {
		this.name = name;
		this.type = type;
	}
	
}
