package com.skyhouse.projectrpg.data;

/**
 * Character data.
 * @author Meranote
 */
public class CharacterData extends Data {
	
	public String name;
	public float x, y;
	public boolean flipX = false;
	public ActionState state = ActionState.IDLE;
	public int[] equip;
	
	/**
	 * Construct a new character data.
	 */
	public CharacterData() {
		equip = new int[7];
	}
	
}