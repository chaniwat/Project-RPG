package com.skyhouse.projectrpg.data;

/**
 * Character data.
 * @author Meranote
 */
public class CharacterData extends ActorData {
	
	public String name;
	public float x, y;
	public boolean flipX = false;
	public ActionState state = ActionState.IDLE;
	public CharacterEquipData equip;
	
	/**
	 * Construct a new character data.
	 */
	public CharacterData() {
		equip = new CharacterEquipData();
	}
	
}