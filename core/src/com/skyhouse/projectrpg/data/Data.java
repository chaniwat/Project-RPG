package com.skyhouse.projectrpg.data;

/**
 * Abstract class of any data.
 * @author Meranote
 */
public abstract class Data {

	/**
	 * Action state of character and monster.
	 * @author Meranote
	 */
	public static enum ActionState {
		IDLE, WALK, JUMP, FALL, ACTION, DEATH
	}
	
}
