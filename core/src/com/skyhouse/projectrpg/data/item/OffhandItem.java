package com.skyhouse.projectrpg.data.item;

public class OffhandItem extends BaseItem {

	public static enum OffhandType {
		SHIELD,
		QUIVER,
		BOOK;
		
		public static ItemBaseType getItemBaseType() {
			return ItemBaseType.OFFHAND;
		}
	}
	
	public OffhandType type;
	
	public OffhandItem(String name, OffhandType type) {
		super(name, OffhandType.getItemBaseType());
	}

}
