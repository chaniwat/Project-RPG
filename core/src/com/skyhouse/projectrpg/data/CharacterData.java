package com.skyhouse.projectrpg.data;

/**
 * Character data.
 * @author Meranote
 */
public class CharacterData extends Data {
	
	public float x, y;
	public boolean flipX = false;
	public ActionState state = ActionState.IDLE;
	
}