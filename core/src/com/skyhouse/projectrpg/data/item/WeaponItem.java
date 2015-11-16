package com.skyhouse.projectrpg.data.item;

public class WeaponItem extends ShowEquipItem {

	public static enum WeaponType {
		SWORD,
		BOW,
		STAFF;
		
		public static ItemBaseType getItemBaseType() {
			return ItemBaseType.WEAPON;
		}
	}
	
	public WeaponType type;
	
	public WeaponItem(String name, WeaponType type) {
		super(name, WeaponType.getItemBaseType());
	}

}
