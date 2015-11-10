package com.skyhouse.projectrpg.data.item;

public class BaseItem {
	
	public static enum ItemType {
		POTION,
		QUEST,
		WEAPON,
		OFFHAND,
		HELMET,
		ARMOR,
		PANT,
		GLOVE,
		BOOT,
		NECK,
		EAR,
		RING,
		SWORD,
		BOW,
		STAFF,
		SHIELD,
		QUIVER,
		BOOK;
	}

	public int id, maxstack;
	public String name, description, pathtoicon, pathtotexture;
	
	public BaseItem(int id) {
		this.id = id;
	}
	
}
